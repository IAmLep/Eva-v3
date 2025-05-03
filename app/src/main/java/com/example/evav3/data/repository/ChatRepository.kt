package com.example.evav3.data.repository

import com.example.evav3.data.model.ChatResponse

interface ChatRepository {
    suspend fun sendMessage(message: String): Result<ChatResponse>
}