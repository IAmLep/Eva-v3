package com.example.evav3.ui.chat

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.evav3.R
import com.example.evav3.model.ChatMessage
import io.noties.markwon.Markwon
import io.noties.markwon.html.HtmlPlugin
import java.text.SimpleDateFormat
import java.util.Locale

class ChatAdapter : ListAdapter<ChatMessage, ChatAdapter.ChatViewHolder>(ChatMessageDiffCallback()) {

    private var markwon: Markwon? = null

    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
        private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        class ChatMessageDiffCallback : DiffUtil.ItemCallback<ChatMessage>() {
            override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
                // Use the unique ID to check if items are the same
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
                // Content comparison remains the same (checks text, sender, timestamp)
                return oldItem == newItem
            }
        }
    }

    // ViewHolder remains the same
    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageBody: TextView = itemView.findViewById(R.id.text_message_body)
        val messageRoot: LinearLayout = itemView.findViewById(R.id.message_root)
        val timestampText: TextView = itemView.findViewById(R.id.text_message_timestamp)
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).isSentByUser) VIEW_TYPE_SENT else VIEW_TYPE_RECEIVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val context = parent.context
        if (markwon == null) {
            markwon = Markwon.builder(context)
                .usePlugin(HtmlPlugin.create())
                .build()
        }
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_chat_message, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = getItem(position)

        markwon?.setMarkdown(holder.messageBody, message.text) ?: run {
            holder.messageBody.text = message.text
        }

        holder.timestampText.text = timeFormat.format(message.timestamp)
        holder.timestampText.visibility = View.VISIBLE

        val context = holder.itemView.context
        val layoutParams = holder.timestampText.layoutParams as LinearLayout.LayoutParams

        if (message.isSentByUser) {
            holder.messageRoot.gravity = android.view.Gravity.END
            holder.messageBody.background = ContextCompat.getDrawable(context, R.drawable.bg_chat_bubble_sent)
            layoutParams.gravity = android.view.Gravity.START
        } else {
            holder.messageRoot.gravity = android.view.Gravity.START
            holder.messageBody.background = ContextCompat.getDrawable(context, R.drawable.bg_chat_bubble_received)
            layoutParams.gravity = android.view.Gravity.END
        }
        holder.timestampText.layoutParams = layoutParams
    }
    // No changes needed below this line for this step
}