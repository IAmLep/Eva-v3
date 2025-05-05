package com.example.evav3.ui // Or viewmodel package

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evav3.data.repository.ChatRepository // Correct repository
import com.example.evav3.model.ChatMessage // Your UI model
// Import the DTO from data.remote.dto
import com.example.evav3.data.remote.dto.ConversationResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.Result
import java.util.Date

@HiltViewModel
class MainViewModel @Inject constructor(
    private val chatRepository: ChatRepository
    // private val conversationRepository: ConversationRepository // Keep if needed for local DB
) : ViewModel() {

    private val _messages = MutableLiveData<List<ChatMessage>>(emptyList())
    val messages: LiveData<List<ChatMessage>> = _messages

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    private var currentSessionId: String? = null // To be passed to repository
    private var isAuthenticated = false

    fun onUserAuthenticated() {
        isAuthenticated = true
        Timber.i("ViewModel notified: User is authenticated.")
    }

    fun sendMessage(messageText: String) {
        if (!isAuthenticated) {
            _error.value = "Cannot send message: Not authenticated."
            Timber.w("Attempted to send message before authentication completed.")
            return
        }
        if (_isLoading.value == true) return

        val userMessage = ChatMessage(
            text = messageText,
            isSentByUser = true,
            timestamp = Date()
        )
        addMessageToList(userMessage)

        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            // Call repository with message AND currentSessionId
            val result = chatRepository.sendMessage(message = messageText, sessionId = currentSessionId) // <<< Pass sessionId

            _isLoading.value = false

            result.fold(
                onSuccess = { conversationResponse -> // <<< Expecting ConversationResponse
                    // Update session ID from the response DTO
                    currentSessionId = conversationResponse.sessionId
                    Timber.d("Updated session ID to: %s", currentSessionId)

                    // Use the 'response' field from the DTO (or adjust if your DTO field name is different)
                    val aiResponseText = conversationResponse.response ?: "Empty response"

                    val aiMessage = ChatMessage(
                        text = aiResponseText, // <<< Use DTO's response field
                        isSentByUser = false,
                        timestamp = Date()
                    )
                    addMessageToList(aiMessage)
                },
                onFailure = { exception ->
                    Timber.e(exception, "Error sending message")
                    _error.value = "Error: ${exception.message ?: "Unknown network error"}"
                    // Optionally add error to chat list
                }
            )
        }
    }

    fun clearError() {
        _error.value = null
    }

    private fun addMessageToList(message: ChatMessage) {
        _messages.value = _messages.value?.plus(message) ?: listOf(message)
    }
}