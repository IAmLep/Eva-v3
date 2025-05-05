package com.example.evav3.data.repository

// Import the DTO from data.remote.dto
import com.example.evav3.data.remote.dto.ConversationResponse
import kotlin.Result // Use Kotlin's Result

interface ChatRepository {
    /**
     * Sends a message to the backend API.
     * @param message The text message from the user.
     * @param sessionId The optional current session ID.
     * @return A Result wrapping the ConversationResponse DTO on success, or an Exception on failure.
     */
    suspend fun sendMessage(message: String, sessionId: String?): Result<ConversationResponse> // <<< Use DTO and add sessionId
}