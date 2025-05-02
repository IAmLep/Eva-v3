package com.example.evav3.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.evav3.data.ChatRepository
import com.example.evav3.data.SimulatedChatRepository

/**
 * Factory for creating ChatViewModel instances with a ChatRepository dependency.
 */
class ChatViewModelFactory(
    private val chatRepository: ChatRepository = SimulatedChatRepository() // Provide default for simplicity
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            // Create ChatViewModel, passing the repository instance
            return ChatViewModel(chatRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}