package com.example.evav3.data.remote

import com.example.evav3.data.repository.AuthRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val authRepository: AuthRepository
) : Interceptor {

    // No excluded paths needed if ALL backend endpoints require auth
    // private val excludedPaths = setOf<String>()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Attempt to get token only if user is signed in (should be after anonymous sign-in)
        val currentUser = authRepository.getCurrentUser()
        if (currentUser == null) {
            Timber.w("Proceeding without auth token: No user signed in. Path: %s", originalRequest.url.encodedPath)
            return chain.proceed(originalRequest)
        }

        // runBlocking is used here for simplicity in the interceptor context.
        // Ensure AuthRepository handles potential exceptions during token fetch.
        // A more robust solution might involve asynchronous token refresh logic.
        val token = runBlocking {
            authRepository.getUserIdToken()
        }

        if (token == null) {
            Timber.e("Auth token fetch failed or returned null. Proceeding without Authorization header. Path: %s", originalRequest.url.encodedPath)
            // Critical decision: Fail fast or proceed? Proceeding will likely cause 401/403 from backend.
            // Failing fast might be better to signal the issue immediately.
            // return chain.proceed(originalRequest) // Proceed (likely fails)
            throw IOException("Failed to retrieve valid authentication token for ${originalRequest.url}") // Fail fast
        }

        Timber.d("Adding Auth token for path: %s", originalRequest.url.encodedPath)
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(newRequest)
    }
}