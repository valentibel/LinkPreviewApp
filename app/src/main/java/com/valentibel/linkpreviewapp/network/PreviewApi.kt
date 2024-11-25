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
    suspend fun postPreview(@Query("q") url: String, @Query("fields") fields: String)

}