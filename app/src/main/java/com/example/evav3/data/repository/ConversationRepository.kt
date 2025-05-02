package com.example.evav3.data.repository

import com.example.evav3.data.remote.api.ConversationApi
import com.example.evav3.data.remote.dto.ConversationRequest
import com.example.evav3.data.remote.dto.ConversationResponse
import com.example.evav3.utils.Result // A simple wrapper class for results
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConversationRepository @Inject constructor(
    private val conversationApi: ConversationApi
    // Inject AuthRepository ONLY IF you need the token explicitly here,
    // otherwise the Interceptor handles it automatically.
    // private val authRepository: AuthRepository
) {

    suspend fun sendMessage(message: String, sessionId: String?): Result<ConversationResponse> {
        // Execute network call on IO dispatcher
        return withContext(Dispatchers.IO) {
            try {
                val request = ConversationRequest(message = message, sessionId = sessionId)
                Timber.d("Sending message: %s, Session ID: %s", message, sessionId)
                val response = conversationApi.sendMessage(request)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Timber.d("Received successful response: %s", body)
                        Result.Success(body)
                    } else {
                        Timber.e("API call successful but response body is null. Code: %d", response.code())
                        Result.Error("Empty response from server")
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Timber.e("API call failed. Code: %d, Error: %s", response.code(), errorBody)
                    // You might want to parse the errorBody if it's structured (e.g., JSON)
                    Result.Error("Error ${response.code()}: $errorBody")
                }
            } catch (e: Exception) {
                Timber.e(e, "Network or unexpected error during sendMessage")
                Result.Error("Network error: ${e.message ?: "Unknown"}")
            }
        }
    }
}

// Helper Result class (place in a 'utils' package)
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val message: String, val exception: Exception? = null) : Result<Nothing>()
}
