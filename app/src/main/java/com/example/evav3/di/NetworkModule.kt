package com.example.evav3.di

import com.example.evav3.BuildConfig // Import BuildConfig to check build type
import com.example.evav3.data.remote.AuthInterceptor // Your existing AuthInterceptor
import com.example.evav3.network.ApiService // Your ApiService interface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Hilt module for providing network-related dependencies (OkHttpClient, Retrofit, ApiService).
 */
@Module
@InstallIn(SingletonComponent::class) // Scope dependencies to the application lifecycle
object NetworkModule {

    // --- Configuration Constants ---
    // Replace with your actual backend URL.
    // 10.0.2.2 is the standard way to access the host machine's localhost from the Android emulator.
    private const val BASE_URL = "http://10.0.2.2:5000/" // Example: Common for local Flask/Python backend
    private const val TIMEOUT_SECONDS = 30L // Connection/Read/Write timeout duration

    /**
     * Provides a singleton instance of the HttpLoggingInterceptor.
     * Logs network request/response details (useful for debugging).
     * Logging level is set based on the build type (BODY for debug, NONE for release).
     * @return The configured HttpLoggingInterceptor instance.
     */
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY // Log everything in debug builds
            } else {
                HttpLoggingInterceptor.Level.NONE // Don't log details in release builds
            }
        }
    }

    /**
     * Provides a singleton instance of the AuthInterceptor.
     * Assumes AuthInterceptor has a constructor that Hilt can manage.
     * If AuthInterceptor requires its own dependencies (e.g., an AuthRepository or Context),
     * they need to be passed as parameters here and provided elsewhere by Hilt.
     * @return The AuthInterceptor instance.
     */
    @Provides
    @Singleton
    fun provideAuthInterceptor(/* Inject dependencies needed by AuthInterceptor here if any */): AuthInterceptor {
        // Example: if it needs a token provider: provideAuthInterceptor(tokenProvider: TokenProvider)
        return AuthInterceptor(/* pass dependencies if needed */)
    }

    /**
     * Provides a singleton instance of OkHttpClient.
     * Configured with logging and authentication interceptors and timeouts.
     * @param loggingInterceptor The logging interceptor provided by Hilt.
     * @param authInterceptor The authentication interceptor provided by Hilt.
     * @return The configured OkHttpClient instance.
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)       // Log network traffic
            .addInterceptor(authInterceptor)          // Add authentication headers/tokens
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    /**
     * Provides a singleton instance of Retrofit.
     * Configured with the base URL, the custom OkHttpClient, and a Gson converter.
     * @param okHttpClient The OkHttpClient instance provided by Hilt.
     * @return The configured Retrofit instance.
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Use the OkHttpClient with interceptors
            .addConverterFactory(GsonConverterFactory.create()) // Use Gson for JSON serialization/deserialization
            .build()
    }

    /**
     * Provides a singleton instance of the ApiService interface implementation.
     * Retrofit creates the actual implementation based on the interface definition.
     * @param retrofit The Retrofit instance provided by Hilt.
     * @return The ApiService implementation instance.
     */
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}