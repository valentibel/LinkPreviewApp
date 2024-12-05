package com.valentibel.linkpreviewapp

//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import com.valentibel.linkpreviewapp.data.PreviewUiState
import com.valentibel.linkpreviewapp.model.PreviewItem
import com.valentibel.linkpreviewapp.repository.PreviewRepository
import com.valentibel.linkpreviewapp.screens.home.HomeScreenViewModel
import retrofit2.HttpException

@OptIn(ExperimentalCoroutinesApi::class)
class HomeScreenViewModelTest {

   // @get:Rule
    //val instantExecutorRule = InstantTaskExecutorRule() // Ensures LiveData updates synchronously for testing

    private val repository: PreviewRepository = mock(PreviewRepository::class.java)
    private val dispatcher = StandardTestDispatcher()
    private val viewModel = HomeScreenViewModel(repository)

    @Test
    fun `loadPreview sets Loading state and updates with Success`() = runTest(dispatcher) {
        val mockUrl = "http://example.com"
        val mockData = PreviewItem(
            description = "Example description",
            image = "http://example.com/example.jpg",
            title = "Example title",
            url = "http://example.com"
        )

            `when`(repository.getPreview(mockUrl)).thenReturn(mockData)

        viewModel.loadPreview(mockUrl)

        // Assert initial loading state
        assertEquals(PreviewUiState.Loading, viewModel.uiState.first())

        dispatcher.scheduler.advanceUntilIdle() // Simulate coroutine completion

        // Assert successful state
        assertEquals(PreviewUiState.Success(mockData), viewModel.uiState.first())
    }

    @Test
    fun `loadPreview handles Exception and sets Error state`() = runTest(dispatcher) {
        val mockUrl = "http://invalid-url.com"

        `when`(repository.getPreview(mockUrl)).thenThrow(Exception())

        viewModel.loadPreview(mockUrl)

        // Assert loading state
        assertEquals(PreviewUiState.Loading, viewModel.uiState.first())

        // Simulate coroutine completion
        dispatcher.scheduler.advanceUntilIdle()

        // Assert error state
        assert(viewModel.uiState.first() is PreviewUiState.Error)
    }
}
