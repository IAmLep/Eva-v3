package com.example.evav3.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evav3.data.repository.ChatRepository
import com.example.evav3.data.repository.ConversationRepository
import com.example.evav3.data.remote.dto.ConversationResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.Result

@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val conversationRepository: ConversationRepository
) : ViewModel() {

    private val _reply = MutableLiveData<String>()
    val reply: LiveData<String> = _reply

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var currentSessionId: String? = null

    fun sendMessage(message: String) {
        if (message.isBlank()) {
            _error.value = "Message cannot be empty."
            return
        }
        _isLoading.value = true
        viewModelScope.launch {
            val result: Result<ConversationResponse> = chatRepository.sendMessage(message, currentSessionId)
            _isLoading.value = false
            result.fold(
                onSuccess = { response: ConversationResponse ->
                    _reply.value = response.response
                },
                onFailure = { exception ->
                    _error.value = "Exception: ${exception.message ?: "Unknown error"}"
                }
            )
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