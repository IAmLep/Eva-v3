package com.example.evav3.di

import com.example.evav3.data.repository.ChatRepository
import com.example.evav3.data.repository.ChatRepositoryImpl
import com.example.evav3.data.repository.ConversationRepository
import com.example.evav3.data.repository.ConversationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Assuming repositories should be singletons
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindChatRepository(
        chatRepositoryImpl: ChatRepositoryImpl
    ): ChatRepository

    @Binds
    @Singleton
    abstract fun bindConversationRepository(
        conversationRepositoryImpl: ConversationRepositoryImpl
    ): ConversationRepository
}