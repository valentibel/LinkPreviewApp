package com.valentibel.linkpreviewapp.repository

import android.util.Log
import com.valentibel.linkpreviewapp.model.PreviewItem
import com.valentibel.linkpreviewapp.network.PreviewApi
import com.valentibel.linkpreviewapp.network.PreviewApi.Companion.errorMap
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PreviewRepository @Inject constructor(private val api: PreviewApi) {

    suspend fun getPreview(url: String): Result<PreviewItem> =
        try {
            val previewItem = api.getPreview(url)
            Result.success(previewItem)
        } catch (e: HttpException) {
            val message = errorMap.getOrDefault(e.code(), "Error occurred")
            Result.failure(Throwable(message))
        } catch (e: IOException) {
            Result.failure(Throwable("Network error occurred"))
        } catch (e: Exception) {
            Result.failure(e)
        }


    suspend fun postPreview(url: String, fields: String): Result<PreviewItem> =
        try {
            val previewItem = api.postPreview(url, fields)
            Result.success(previewItem)
        } catch (e: HttpException) {
            val message = errorMap.getOrDefault(e.code(), "Error occurred")
            Result.failure(Throwable(message))
        } catch (e: IOException) {
            Result.failure(Throwable("Network error occurred"))
        } catch (e: Exception) {
            Result.failure(e)
        }

}