package com.example.evav3.model

import java.util.Date
import java.util.UUID

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(), // Unique ID
    val text: String,
    val isSentByUser: Boolean, // True for user message, false for AI
    val timestamp: Date,       // Timestamp of the message
    val isError: Boolean = false // Optional: Flag to indicate if this is an error message for UI styling
)