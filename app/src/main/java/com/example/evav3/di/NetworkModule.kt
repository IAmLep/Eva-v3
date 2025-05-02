package com.example.evav3.di

import com.example.evav3.network.ApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Provides dependencies for the entire application lifecycle
object NetworkModule {

    // Base URL for your backend API
    // IMPORTANT: Replace with your actual backend URL (e.g., Cloud Run URL)
    // Make sure it ends with a '/'
    private const val BASE_URL = "http://10.0.2.2:8000/api/v1/conversation/" // Example for local emulator

    @Provides
    @Singleton
    fun provideGson(): Gson {
        // Configure Gson if needed (e.g., date formats, custom type adapters)
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        // Add logging interceptor for debugging network requests (optional)
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Log request/response body
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // Add the interceptor
            // Add other configurations like timeouts, authenticators if needed
            // .connectTimeout(30, TimeUnit.SECONDS)
            // .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Use the custom OkHttpClient
            .addConverterFactory(GsonConverterFactory.create(gson)) // Use Gson for JSON parsing
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        // Create the ApiService implementation using Retrofit
        return retrofit.create(ApiService::class.java)
    }
}