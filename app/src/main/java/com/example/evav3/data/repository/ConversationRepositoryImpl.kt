package com.example.evav3.data.repository

import com.example.evav3.data.model.Conversation
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject // Added import
import javax.inject.Singleton

@Singleton
class ConversationRepositoryImpl @Inject constructor( // Added @Inject
    private val conversationDao: ConversationDao
) : ConversationRepository {
    override fun getAllConversations(): Flow<List<Conversation>> {
        return conversationDao.getAllConversations()
    }

    override suspend fun insertConversation(conversation: Conversation) {
        conversationDao.insert(conversation)
    }

    override suspend fun deleteConversation(conversation: Conversation) {
        conversationDao.delete(conversation)
    }
}