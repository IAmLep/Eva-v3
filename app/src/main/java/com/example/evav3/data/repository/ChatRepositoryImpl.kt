package com.example.evav3.data.repository

import com.example.evav3.network.ApiService
import com.example.evav3.data.model.ChatResponse
import javax.inject.Inject // Added import
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor( // Added @Inject
    private val apiService: ApiService
) : ChatRepository {
    override suspend fun sendMessage(message: String): Result<ChatResponse> {
        return try {
            val response = apiService.sendMessage(ChatRequest(message))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}