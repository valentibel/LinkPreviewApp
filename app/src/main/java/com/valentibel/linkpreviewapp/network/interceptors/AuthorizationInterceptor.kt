package com.valentibel.linkpreviewapp.network.interceptors

import com.valentibel.linkpreviewapp.utils.Constants.API_KEY
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val request = originalRequest.newBuilder()
            .header("X-Linkpreview-Api-Key", API_KEY)
            .build()

        return chain.proceed(request)

    }
}