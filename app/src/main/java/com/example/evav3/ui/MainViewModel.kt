package com.example.evav3.ui // Or viewmodel package

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evav3.data.model.ChatMessage // Create this data class
import com.example.evav3.data.repository.ConversationRepository
import com.example.evav3.utils.Result // Import the Result wrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val conversationRepository: ConversationRepository
) : ViewModel() {

    private val _messages = MutableLiveData<List<ChatMessage>>(emptyList())
    val messages: LiveData<List<ChatMessage>> = _messages

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    private var currentSessionId: String? = null
    private var isAuthenticated = false

    // Called from Activity after anonymous sign-in succeeds
    fun onUserAuthenticated() {
        isAuthenticated = true
        // Optionally load initial data or conversation history here
        Timber.i("ViewModel notified: User is authenticated.")
    }

    fun sendMessage(messageText: String) {
        if (!isAuthenticated) {
            _error.value = "Cannot send message: Not authenticated."
            Timber.w("Attempted to send message before authentication completed.")
            return
        }
        if (_isLoading.value == true) return // Prevent multiple simultaneous requests

        val userMessage = ChatMessage(text = messageText, isUser = true, timestamp = System.currentTimeMillis())
        addMessageToList(userMessage)

        _isLoading.value = true
        _error.value = null // Clear previous error

        viewModelScope.launch {
            when (val result = conversationRepository.sendMessage(messageText, currentSessionId)) {
                is Result.Success -> {
                    val response = result.data
                    currentSessionId = response.sessionId // Update session ID
                    val aiResponseText = response.response ?: "Empty response" // Handle null response
                    val aiMessage = ChatMessage(text = aiResponseText, isUser = false, timestamp = System.currentTimeMillis())
                    addMessageToList(aiMessage)
                    Timber.d("AI Response added. New Session ID: %s", currentSessionId)
                }
                is Result.Error -> {
                    Timber.e("Error sending message: %s", result.message)
                    _error.value = "Error: ${result.message}"
                    // Optional: Add an error message to the chat list?
                    // addMessageToList(ChatMessage(text="Error: ${result.message}", isUser = false, isError = true))
                }
            }
            _isLoading.value = false
        }
    }

    fun clearError() {
        _error.value = null
    }

    private fun addMessageToList(message: ChatMessage) {
        _messages.value = _messages.value?.plus(message)
    }
}

// Create this data class (e.g., in data/model package)
data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(), // Simple unique ID
    val text: String,
    val isUser: Boolean, // True for user message, false for AI
    val timestamp: Long,
    val isError: Boolean = false // Optional: Flag for error messages
)