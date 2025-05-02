package com.example.evav3.ui.chat // Or the correct package

import androidx.lifecycle.ViewModel
import com.example.evav3.data.repository.ChatRepository // Import your repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    // Your ViewModel logic using chatRepository goes here.

}