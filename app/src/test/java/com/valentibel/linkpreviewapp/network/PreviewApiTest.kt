package com.valentibel.linkpreviewapp.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalCoroutinesApi::class)
class PreviewApiTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var mockWebServer: MockWebServer
    private lateinit var retrofit: Retrofit

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockWebServer = MockWebServer()
        mockWebServer.start()

        retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mockWebServer.shutdown()
    }

    @Test
    fun `getPreview success`() = runTest {
        //Given
        val url = "http://example.com"
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody("{\"title\":\"Title\",\"description\":\"Description\",\"image\":\"https://image.jpg\",\"url\":\"$url\"}"))
        val previewApi = retrofit.create(PreviewApi::class.java)

        //Then
        val response = previewApi.getPreview(url)

        //When
        assertNotNull(response)
        assertEquals(url, response.url)
    }

    @Test(expected = HttpException::class)
    fun `getPreview failure`() = runTest {
        //Given
        val url = "http://wrong.com"
        val errorCode = 401
        mockWebServer.enqueue(MockResponse().setResponseCode(errorCode).setBody("{\"title\":\"\",\"description\":\"\",\"image\":\"\",\"url\":\"\",\"error\":\"$errorCode\"}"))
        val previewApi = retrofit.create(PreviewApi::class.java)

        //Then
        previewApi.getPreview(url)
    }

    @Test
    fun `postPreview success`() = runTest {
        //Given
        val url = "http://example.com"
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody("{\"title\":\"Title\",\"description\":\"Description\",\"image\":\"https://image.jpg\",\"url\":\"$url\"}"))
        val previewApi = retrofit.create(PreviewApi::class.java)

        //Then
        val response = previewApi.postPreview(url, "")

        //When
        assertNotNull(response)
        assertEquals(url, response.url)
    }

    @Test(expected = HttpException::class)
    fun `postPreview failure`() = runTest {
        //Given
        val url = "http://wrong.com"
        val errorCode = 401
        mockWebServer.enqueue(MockResponse().setResponseCode(errorCode).setBody("{\"title\":\"\",\"description\":\"\",\"image\":\"\",\"url\":\"\",\"error\":\"$errorCode\"}"))
        val previewApi = retrofit.create(PreviewApi::class.java)

        //Then
        previewApi.postPreview(url, "")
    }
}