package com.example.evav3.network

// Import the DTOs from data.remote.dto
import com.example.evav3.data.remote.dto.ConversationRequest
import com.example.evav3.data.remote.dto.ConversationResponse
import retrofit2.Response // Keep using Retrofit's Response wrapper if your API might return non-2xx codes with a body
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Defines the network API endpoints using DTOs from data.remote.dto.
 */
interface ApiService {

    /**
     * Sends a chat message to the backend using ConversationRequest DTO.
     * @param request The ConversationRequest object containing the message and optional session ID.
     * @return A Retrofit Response wrapping the ConversationResponse DTO from the backend.
     */
    // Ensure this path ("/chat" or "/api/v1/conversation/") matches your backend endpoint for this call.
    @POST("chat") // <<< Or use "/api/v1/conversation/" if that's correct
    suspend fun sendMessage(@Body request: ConversationRequest): Response<ConversationResponse> // <<< Use Response<DTO>

    // Add other API calls here using appropriate DTOs
}