package com.example.evav3.network

import com.google.gson.annotations.SerializedName // Import SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// Updated data class based on api.py ConversationRequest
data class ConversationRequest(
    @SerializedName("message") val message: String,
    @SerializedName("session_id") val sessionId: String?, // Optional in Python, nullable in Kotlin
    @SerializedName("metadata") val metadata: Map<String, Any>? // Maps to Dict[str, Any]?, Any is flexible
)

// Updated data class based on api.py ConversationResponse
data class ConversationResponse(
    @SerializedName("response") val response: String,
    @SerializedName("session_id") val sessionId: String, // Non-optional in Python
    @SerializedName("function_calls") val functionCalls: List<Map<String, Any>>?, // Maps to List[Dict[str, Any]]?
    @SerializedName("error") val error: String? // Optional in Python, nullable in Kotlin
)

interface ApiService {

    /**
     * Sends a message to the backend conversation endpoint.
     * Assumes the base URL configured in Retrofit ends with "/api/v1/conversation/"
     */
    @POST("/") // Path relative to the base URL
    suspend fun postConversation(
        @Body requestBody: ConversationRequest
    ): Response<ConversationResponse>

    // Add other API endpoint definitions here if needed
}