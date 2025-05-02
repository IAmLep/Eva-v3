package com.example.evav3.data.remote.api

import com.example.evav3.data.remote.dto.ConversationRequest
import com.example.evav3.data.remote.dto.ConversationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ConversationApi {

    @POST("api/v1/conversation/") // Ensure this path is correct
    suspend fun sendMessage(
        @Body request: ConversationRequest
    ): Response<ConversationResponse>
}