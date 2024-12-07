package com.valentibel.linkpreviewapp.network

import com.valentibel.linkpreviewapp.model.PreviewItem
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface PreviewApi {

    @GET(".")
    suspend fun getPreview(@Query("q") url: String): PreviewItem

    @POST(".")
    suspend fun postPreview(@Query("q") url: String, @Query("fields") fields: String): PreviewItem

    companion object {
        val errorMap = hashMapOf(
            401 to "Access key error",
            403 to "Access key error",
            423 to "Forbidden by robots.txt - the requested website does not allow us to access this page",
            425 to "Invalid response status code (with the actual response code we got from the remote server)",
            426 to "Too many requests",
            429 to "Too many requests"
        )
    }
}