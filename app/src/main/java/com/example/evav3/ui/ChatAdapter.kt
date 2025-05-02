package com.example.evav3.ui // Or adapter package

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout // Use FrameLayout.LayoutParams for gravity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.evav3.R
import com.example.evav3.data.model.ChatMessage
import com.example.evav3.databinding.ItemChatMessageBinding // Import ViewBinding class

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

            // Adjust appearance based on whether it's a user or AI message
            val context = binding.root.context
            val messageLayoutParams = binding.messageTextView.layoutParams as FrameLayout.LayoutParams // Use FrameLayout or LinearLayout params

            if (chatMessage.isUser) {
                // User message: Align right, different background
                messageLayoutParams.gravity = Gravity.END
                binding.messageTextView.background = ContextCompat.getDrawable(context, R.drawable.message_bubble_background_user)
                binding.messageTextView.setTextColor(ContextCompat.getColor(context, android.R.color.black)) // Or specific color
            } else {
                // AI message: Align left, different background
                messageLayoutParams.gravity = Gravity.START
                binding.messageTextView.background = ContextCompat.getDrawable(context, R.drawable.message_bubble_background_ai)
                binding.messageTextView.setTextColor(ContextCompat.getColor(context, android.R.color.black)) // Or specific color

                // Optional: Handle error message styling
                if (chatMessage.isError) {
                    binding.messageTextView.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark))
                    // You could also change the background for errors
                }
            }
            binding.messageTextView.layoutParams = messageLayoutParams // Re-apply layout params

            // Optional: Bind timestamp if you added the TextView
            // binding.timestampTextView.text = formatTimestamp(chatMessage.timestamp)
            // val timestampLayoutParams = binding.timestampTextView.layoutParams as FrameLayout.LayoutParams
            // timestampLayoutParams.gravity = messageLayoutParams.gravity // Align timestamp too
            // binding.timestampTextView.layoutParams = timestampLayoutParams
        }
    }

    // Optional: Timestamp formatting function
    /*
    private fun formatTimestamp(timestamp: Long): String {
        // Implement your desired timestamp formatting logic here
        // Example: return SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(timestamp))
        return android.text.format.DateFormat.getTimeFormat(itemView.context).format(Date(timestamp))
    }
    */
}

// DiffUtil for efficient list updates
class MessageDiffCallback : DiffUtil.ItemCallback<ChatMessage>() {
    override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
        return oldItem.id == newItem.id // Use unique ID
    }

    override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
        return oldItem == newItem // Rely on data class equals()
    }
}