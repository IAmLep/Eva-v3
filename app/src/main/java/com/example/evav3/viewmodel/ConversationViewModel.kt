package com.example.evav3.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evav3.network.ConversationResponse
import com.example.evav3.repository.ConversationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing conversation data and interactions.
 * It communicates with the ConversationRepository to send messages
 * and exposes the results to the UI via LiveData.
 */
@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val repository: ConversationRepository
) : ViewModel() {

    // LiveData to hold the latest successful conversation response
    private val _conversationResponse = MutableLiveData<ConversationResponse>()
    val conversationResponse: LiveData<ConversationResponse> = _conversationResponse

    // LiveData to hold any error messages
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    // LiveData to indicate if a network request is in progress
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // Variable to hold the current session ID (can be updated)
    private var currentSessionId: String? = null

    /**
     * Sends a message to the backend via the repository.
     * Updates LiveData based on the success or failure of the call.
     *
     * @param message The message text to send.
     * @param metadata Optional metadata for the request.
     */
    fun sendMessage(message: String, metadata: Map<String, Any>? = null) {
        // Don't send empty messages
        if (message.isBlank()) {
            _error.value = "Message cannot be empty."
            return
        }

        _isLoading.value = true // Indicate loading state

        // Launch a coroutine in the ViewModel's scope
        viewModelScope.launch {
            try {
                // Call the repository's suspend function
                val response = repository.sendMessage(
                    message = message,
                    sessionId = currentSessionId, // Use the current session ID
                    metadata = metadata
                )

                if (response.isSuccessful && response.body() != null) {
                    val conversationData = response.body()!!
                    if (conversationData.error == null) {
                        // Success: Update response LiveData and store the new session ID
                        _conversationResponse.value = conversationData
                        currentSessionId = conversationData.sessionId // Update session ID for next message
                    } else {
                        // Handle backend-specific error message
                        _error.value = "API Error: ${conversationData.error}"
                    }
                } else {
                    // Handle network or HTTP error
                    _error.value = "Error: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                // Handle exceptions (e.g., network issues)
                _error.value = "Exception: ${e.message ?: "Unknown error"}"
            } finally {
                _isLoading.value = false // Reset loading state
            }
        }
    }

    /**
     * Clears the current session ID, effectively starting a new conversation
     * on the next message send.
     */
    fun startNewSession() {
        currentSessionId = null
        // Optionally clear previous responses or errors from LiveData
        // _conversationResponse.value = null
        // _error.value = null
    }

    /**
     * Sets a specific session ID to continue an existing conversation.
     * @param sessionId The ID of the session to resume.
     */
    fun setSessionId(sessionId: String?) {
        currentSessionId = sessionId
    }

    /**
     * Gets the current session ID being used by the ViewModel.
     */
    fun getCurrentSessionId(): String? {
        return currentSessionId
    }
}