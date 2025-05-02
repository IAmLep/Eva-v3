package com.example.evav3.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ConversationRequest(
    val message: String,
    @SerializedName("session_id")
    val sessionId: String? = null
)

data class ConversationResponse(
    // Adjust based on your actual backend response JSON structure
    val response: String?,
    @SerializedName("session_id")
    val sessionId: String?,
    val error: String? = null // Example error field
    // Add any other fields returned by the backend
)