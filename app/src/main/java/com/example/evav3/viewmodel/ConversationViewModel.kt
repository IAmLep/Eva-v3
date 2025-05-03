package com.example.evav3.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// Import ChatRepository and ChatResponse (assuming ChatResponse is the correct model)
import com.example.evav3.data.model.ChatResponse // Or ConversationResponse if that's truly what ApiService returns
import com.example.evav3.data.repository.ChatRepository // <-- Import ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing *sending* chat messages and observing responses.
 * It communicates with the ChatRepository to send messages
 * and exposes the results to the UI via LiveData.
 */
@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val chatRepository: ChatRepository // <-- Inject ChatRepository
    // If you also need local conversation data:
    // private val conversationRepository: ConversationRepository
) : ViewModel() {

    // LiveData to hold the latest successful chat response
    // Check if ChatResponse or ConversationResponse is the correct model returned by your API
    private val _chatResponse = MutableLiveData<ChatResponse>()
    val chatResponse: LiveData<ChatResponse> = _chatResponse

    // LiveData to hold any error messages
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    // LiveData to indicate if a network request is in progress
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // Variable to hold the current session ID (can be updated)
    // Consider if session ID management belongs here or should be handled differently
    private var currentSessionId: String? = null

    /**
     * Sends a message to the backend via the ChatRepository.
     * Updates LiveData based on the success or failure of the call.
     *
     * @param message The message text to send.
     * @param metadata Optional metadata for the request.
     */
    fun sendMessage(message: String, metadata: Map<String, Any>? = null) {
        if (message.isBlank()) {
            _error.value = "Message cannot be empty."
            return
        }

        _isLoading.value = true

        viewModelScope.launch {
            try {
                // Call the ChatRepository's suspend function
                // Ensure sendMessage in ChatRepository/Impl matches these parameters
                val response: ChatResponse = chatRepository.sendMessage(
                    message = message,
                    sessionId = currentSessionId,
                    metadata = metadata
                )

                // Assuming ChatResponse has 'error' and 'sessionId' fields
                // Adjust logic based on your actual ChatResponse structure
                if (response.error == null) {
                    _chatResponse.value = response
                    currentSessionId = response.sessionId // Update session ID if applicable
                } else {
                    _error.value = "API Error: ${response.error}"
                }

            } catch (e: Exception) {
                _error.value = "Exception: ${e.message ?: "Unknown error"}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun startNewSession() {
        currentSessionId = null
    }

    fun setSessionId(sessionId: String?) {
        currentSessionId = sessionId
    }

    fun getCurrentSessionId(): String? {
        return currentSessionId
    }
}