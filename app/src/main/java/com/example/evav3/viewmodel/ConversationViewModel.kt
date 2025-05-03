package com.example.evav3.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evav3.data.model.ChatResponse // Correct import
import com.example.evav3.data.repository.ChatRepository
import com.example.evav3.data.repository.ConversationRepository // Keep if needed for local data
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.Result // Import Kotlin's Result

@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    // Remove conversationRepository if you don't need local data management in this VM yet
    private val conversationRepository: ConversationRepository
) : ViewModel() {

    // LiveData for the successful reply string
    private val _reply = MutableLiveData<String>()
    val reply: LiveData<String> = _reply

    // LiveData for errors
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    // LiveData for loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // Session ID management might need rethinking if not part of ChatResponse
    private var currentSessionId: String? = null // Keep for now, but API doesn't seem to return it

    // Function signature matches repository (only takes message)
    fun sendMessage(message: String) {
        if (message.isBlank()) {
            _error.value = "Message cannot be empty."
            return
        }
        _isLoading.value = true
        viewModelScope.launch {
            // Call repository function which returns Result<ChatResponse>
            val result: Result<ChatResponse> = chatRepository.sendMessage(message = message)

            _isLoading.value = false // Reset loading state after call

            // Handle the Result using fold
            result.fold(
                onSuccess = { response ->
                    // Success: ChatResponse only has 'reply'
                    _reply.value = response.reply // Post the successful reply

                    // Since ChatResponse doesn't have sessionId or error,
                    // we cannot update currentSessionId from the response
                    // or check response.error here.
                    // If the API *can* return errors, the structure of ChatResponse
                    // or the Result itself needs adjustment in the repository layer.

                    // --- Optionally save conversation locally ---
                    // val newConversation = Conversation(id = currentSessionId ?: System.currentTimeMillis().toString(), /* other fields based on 'message' and 'response.reply' */ )
                    // conversationRepository.insertConversation(newConversation)
                },
                onFailure = { exception ->
                    // Failure: Handle the exception (network error, etc.)
                    _error.value = "Exception: ${exception.message ?: "Unknown error"}"
                }
            )
        }
    }

    fun startNewSession() {
        currentSessionId = null
        // Consider clearing _reply.value = null and _error.value = null here too
    }

    // These session ID functions might be less useful if the API doesn't use/return session IDs
    fun setSessionId(sessionId: String?) {
        currentSessionId = sessionId
    }

    fun getCurrentSessionId(): String? {
        return currentSessionId
    }
}