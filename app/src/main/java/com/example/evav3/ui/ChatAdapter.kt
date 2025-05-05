package com.example.evav3.ui // Or adapter package

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.evav3.R
// Import the correct ChatMessage model
import com.example.evav3.model.ChatMessage // <<< IMPORT CORRECT ChatMessage
import com.example.evav3.databinding.ItemChatMessageBinding
import java.text.DateFormat // For timestamp formatting if needed
import java.util.Date // For timestamp formatting if needed

class ChatAdapter : ListAdapter<ChatMessage, ChatAdapter.MessageViewHolder>(MessageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemChatMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MessageViewHolder(private val binding: ItemChatMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chatMessage: ChatMessage) {
            binding.messageTextView.text = chatMessage.text

            val context = binding.root.context
            val messageLayoutParams = binding.messageTextView.layoutParams as FrameLayout.LayoutParams

            // Use the field name from model/ChatMessage.kt
            if (chatMessage.isSentByUser) { // <<< USE isSentByUser
                // User message
                messageLayoutParams.gravity = Gravity.END
                binding.messageTextView.background = ContextCompat.getDrawable(context, R.drawable.message_bubble_background_user)
                binding.messageTextView.setTextColor(ContextCompat.getColor(context, android.R.color.black))
            } else {
                // AI message
                messageLayoutParams.gravity = Gravity.START
                binding.messageTextView.background = ContextCompat.getDrawable(context, R.drawable.message_bubble_background_ai)
                binding.messageTextView.setTextColor(ContextCompat.getColor(context, android.R.color.black))

                // Optional: Handle error message styling if ChatMessage has isError flag
                // if (chatMessage.isError) {
                //     binding.messageTextView.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark))
                // }
            }
            binding.messageTextView.layoutParams = messageLayoutParams

            // Optional: Bind timestamp if you add a TextView for it
            // binding.timestampTextView.text = formatTimestamp(chatMessage.timestamp)
            // val timestampLayoutParams = binding.timestampTextView.layoutParams as FrameLayout.LayoutParams
            // timestampLayoutParams.gravity = messageLayoutParams.gravity
            // binding.timestampTextView.layoutParams = timestampLayoutParams
        }
    }

    // Optional: Timestamp formatting function
    private fun formatTimestamp(timestamp: Date): String {
        return try {
            DateFormat.getTimeInstance(DateFormat.SHORT).format(timestamp)
        } catch (e: Exception) {
            timestamp.toString() // Fallback
        }
    }
}

// DiffUtil using the correct ChatMessage model
class MessageDiffCallback : DiffUtil.ItemCallback<ChatMessage>() {
    override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
        return oldItem.id == newItem.id // Use unique ID from model
    }

    override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
        return oldItem == newItem // Rely on data class equals()
    }
}