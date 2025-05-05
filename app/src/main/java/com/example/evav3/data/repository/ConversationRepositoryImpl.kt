package com.example.evav3.data.repository

import com.example.evav3.network.ApiService
import com.example.evav3.data.remote.dto.ConversationRequest
import com.example.evav3.data.remote.dto.ConversationResponse
import com.example.evav3.data.model.Conversation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Result

@Singleton
class ConversationRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ConversationRepository {

    override suspend fun sendMessage(message: String, sessionId: String?): Result<ConversationResponse> {
        return try {
            val request = ConversationRequest(message = message, sessionId = sessionId)
            Timber.d("Sending request: %s", request)
            val response = apiService.sendMessage(request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Timber.d("Received successful response: %s", body)
                    Result.success(body)
                } else {
                    Timber.e("API call successful but response body was null.")
                    Result.failure(Exception("Empty response body"))
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                Timber.e("API call failed with code ${response.code()}: $errorBody")
                Result.failure(Exception("API Error ${response.code()}: $errorBody"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Network call failed")
            Result.failure(e)
        }
    }

    override fun getAllConversations(): Flow<List<Conversation>> {
        // TODO: Implement actual data source
        return emptyFlow()
    }

    override suspend fun insertConversation(conversation: Conversation) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteConversation(conversation: Conversation) {
        TODO("Not yet implemented")
    }
}