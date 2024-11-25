package com.valentibel.linkpreviewapp.repository

import android.util.Log
import com.valentibel.linkpreviewapp.model.PreviewItem
import com.valentibel.linkpreviewapp.network.PreviewApi
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PreviewRepository @Inject constructor(private val api: PreviewApi) {

    suspend fun getPreview(url: String): Result<PreviewItem> =
        try {
            val previewItem = api.getPreview(url)
            Result.success(previewItem)
        } catch (e: HttpException) {
            val message = when(e.code()) {
                401, 403 -> "Access key error"
                423 -> "Forbidden by robots.txt - the requested website does not allow us to access this page"
                425 -> "Invalid response status code (with the actual response code we got from the remote server)"
                426, 429 -> "Too many requests"
                else -> "Error occurred"
            }
            Result.failure(Throwable(message))
        } catch (e: IOException) {
            Result.failure(Throwable("Network error occurred"))
        } catch (e: Exception) {
            Result.failure(e)
        }


    suspend fun updatePreview(url: String, fields: String) =
        api.postPreview(url, fields)


}