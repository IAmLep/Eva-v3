package com.example.evav3.data.repository

import com.example.evav3.data.model.Conversation
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining the contract for managing Conversation data.
 * Implementations will handle fetching and manipulating conversations from a data source (e.g., local database, remote API).
 */
interface ConversationRepository {

    /**
     * Retrieves all conversations as a Flow, typically ordered by recency.
     * The Flow allows observing changes to the conversation list over time.
     * @return A Flow emitting a list of all Conversations.
     */
    fun getAllConversations(): Flow<List<Conversation>>

    /**
     * Inserts a new conversation or updates an existing one in the data source.
     * This is a suspending function, intended to be called from a coroutine scope.
     * @param conversation The conversation object to insert or update.
     */
    suspend fun insertConversation(conversation: Conversation)

    /**
     * Deletes a specific conversation from the data source.
     * This is a suspending function, intended to be called from a coroutine scope.
     * @param conversation The conversation object to delete.
     */
    suspend fun deleteConversation(conversation: Conversation)
}