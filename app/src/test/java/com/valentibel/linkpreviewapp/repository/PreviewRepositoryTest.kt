package com.valentibel.linkpreviewapp.repository

import com.valentibel.linkpreviewapp.model.PreviewItem
import com.valentibel.linkpreviewapp.network.PreviewApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.exceptions.base.MockitoException
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class PreviewRepositoryTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var mockApi: PreviewApi
    private lateinit var repository: PreviewRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockApi = mock(PreviewApi::class.java)
        repository = PreviewRepository(mockApi)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getPreview returns success`() = runTest {
        //Given
        val url = "http://example.com"
        val mockItem = PreviewItem(
            description = "Description",
            image = "https://image.jpg",
            title = "Title",
            url = url
        )
        `when`(mockApi.getPreview(url)).thenReturn(mockItem)

        //When
        val result = repository.getPreview(url)

        //Then
        assertTrue(result.isSuccess)
        assertEquals(mockItem, result.getOrNull())
        verify(mockApi, times(1)).getPreview(url)
    }

    @Test
    fun `getPreview returns error`() = runTest {
        //Given
        val url = "http://wrong.com"
        val errorMessage = "Error occurred"
        `when`(mockApi.getPreview(url)).thenThrow(MockitoException(errorMessage))

        //When
        val result = repository.getPreview(url)

        //Then
        assertTrue(result.isFailure)
        assertEquals(errorMessage, result.exceptionOrNull()?.message)
    }

    @Test
    fun `postPreview returns success`() = runTest {
        //Given
        val url = "http://example.com"
        val fields = "title,description,image,url"
        val mockItem = PreviewItem(
            description = "Descroption",
            image = "https://image.jpg",
            title = "Title",
            url = url
        )
        `when`(mockApi.postPreview(url, fields)).thenReturn(mockItem)

        //When
        val result = repository.postPreview(url, fields)

        //Then
        assertTrue(result.isSuccess)
        assertEquals(mockItem, result.getOrNull())
    }

    @Test
    fun `postPreview returns error`() = runTest {
        //Given
        val url = "http://wrong.com"
        val fields = "title,description,image,url"
        val errorMessage = "Error occurred"
        `when`(mockApi.postPreview(url, fields)).thenThrow(MockitoException(errorMessage))

        //When
        val result = repository.postPreview(url, fields)

        //Then
        assertTrue(result.isFailure)
        assertEquals(errorMessage, result.exceptionOrNull()?.message)
    }
}