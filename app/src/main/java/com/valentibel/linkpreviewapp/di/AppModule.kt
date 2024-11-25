package com.valentibel.linkpreviewapp.di

import com.valentibel.linkpreviewapp.network.PreviewApi
import com.valentibel.linkpreviewapp.network.interceptors.AuthorizationInterceptor
import com.valentibel.linkpreviewapp.repository.PreviewRepository
import com.valentibel.linkpreviewapp.utils.Constants.BASE_URL
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePreviewApi(): PreviewApi {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(PreviewApi::class.java)
    }

    @Singleton
    @Provides
    fun providePreviewRepository(api: PreviewApi) = PreviewRepository(api)

}