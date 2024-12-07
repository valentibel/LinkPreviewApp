package com.valentibel.linkpreviewapp.screens.home

import com.valentibel.linkpreviewapp.data.PreviewUiState
import com.valentibel.linkpreviewapp.model.PreviewItem
import com.valentibel.linkpreviewapp.repository.PreviewRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.exceptions.base.MockitoException
import org.mockito.kotlin.times

@OptIn(ExperimentalCoroutinesApi::class)
class HomeScreenViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var repository: PreviewRepository
    private lateinit var viewModel : HomeScreenViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repository = mock(PreviewRepository::class.java)
        viewModel = HomeScreenViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadPreview sets Loading state and updates with Success`() = runTest {

        //Given
        val mockUrl = "http://example.com"
        val mockData = PreviewItem(
            description = "Example description",
            image = "http://example.com/example.jpg",
            title = "Example title",
            url = "http://example.com"
        )
        `when`(repository.getPreview(mockUrl)).thenReturn(Result.success(mockData))

        //When
        viewModel.loadPreview(mockUrl)

        //Then

        // Assert initial loading state
        assertEquals(PreviewUiState.Loading, viewModel.uiState.first())

        // Simulate coroutine completion
        dispatcher.scheduler.advanceUntilIdle()

        // Assert successful state
        assertEquals(PreviewUiState.Success(mockData), viewModel.uiState.first())
        verify(repository, times(1)).getPreview(mockUrl)
    }

    @Test
    fun `loadPreview handles Exception and sets Error state`() = runTest(dispatcher) {
        //Given
        val mockUrl = "http://invalid-url.com"
        `when`(repository.getPreview(mockUrl)).thenReturn(Result.failure(MockitoException("Something is wrong")))

        //When
        viewModel.loadPreview(mockUrl)

        //Then
        // Assert loading state
        assertEquals(PreviewUiState.Loading, viewModel.uiState.first())

        // Simulate coroutine completion
        dispatcher.scheduler.advanceUntilIdle()

        // Assert error state
        assert(viewModel.uiState.first() is PreviewUiState.Error)
        verify(repository, times(1)).getPreview(mockUrl)
    }
}
