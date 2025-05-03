package com.example.evav3.network

import com.example.evav3.data.model.ChatRequest
import com.example.evav3.data.model.ChatResponse
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Defines the network API endpoints for the application using Retrofit annotations.
 */
interface ApiService {

    /**
     * Sends a chat message to the backend.
     * Corresponds to the POST request expected by ChatRepositoryImpl.
     * @param request The ChatRequest object containing the message.
     * @return The ChatResponse object from the backend.
     */
    @POST("chat") // *** IMPORTANT: Replace "chat" with the actual relative path of your chat API endpoint ***
    suspend fun sendMessage(@Body request: ChatRequest): ChatResponse

    // Add definitions for other API calls here, e.g.:
    // @GET("conversations/{id}")
    // suspend fun getConversation(@Path("id") conversationId: String): ConversationDto
    //
    // @POST("login")
    // suspend fun login(@Body loginRequest: LoginRequest): AuthResponse
}