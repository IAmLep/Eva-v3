package com.example.evav3

import android.os.Bundle
import android.view.View // Import View
import android.widget.Toast // Import Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager // Import LinearLayoutManager
import com.example.evav3.databinding.ActivityMainBinding
import com.example.evav3.ui.ChatAdapter // Import ChatAdapter
import com.example.evav3.ui.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var firebaseAuth: FirebaseAuth

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var chatAdapter: ChatAdapter // Declare adapter instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView() // Call setup for RecyclerView
        signInAnonymouslyIfNeeded()
        setupUIObserversAndListeners() // Renamed for clarity
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter() // Initialize adapter
        binding.chatRecyclerView.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@MainActivity).apply {
                stackFromEnd = true // New messages appear at the bottom
            }
        }
    }

    private fun signInAnonymouslyIfNeeded() {
        if (firebaseAuth.currentUser == null) {
            Timber.d("No user signed in. Attempting anonymous sign-in...")
            lifecycleScope.launch {
                try {
                    val authResult = firebaseAuth.signInAnonymously().await()
                    Timber.i("Signed in anonymously successfully. User ID: ${authResult.user?.uid}")
                    viewModel.onUserAuthenticated()
                } catch (e: Exception) {
                    Timber.e(e, "Anonymous sign-in failed")
                    Toast.makeText(this@MainActivity, "Authentication failed. Cannot connect.", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Timber.d("User already signed in. User ID: ${firebaseAuth.currentUser?.uid}")
            viewModel.onUserAuthenticated()
        }
    }

    // Combined UI setup logic
    private fun setupUIObserversAndListeners() {
        // Send Button Listener
        binding.sendButton.setOnClickListener {
            val message = binding.messageInput.text.toString().trim() // Trim whitespace
            if (message.isNotBlank()) {
                viewModel.sendMessage(message)
                binding.messageInput.text.clear() // Clear input
            }
        }

        // Observe messages from ViewModel
        viewModel.messages.observe(this) { messages ->
            chatAdapter.submitList(messages) {
                // Scroll to bottom after list is updated
                if (messages.isNotEmpty()) {
                    binding.chatRecyclerView.smoothScrollToPosition(messages.size - 1)
                }
            }
            Timber.d("Adapter updated with ${messages.size} messages.")
        }

        // Observe loading state
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.sendButton.isEnabled = !isLoading // Disable send button while loading
            binding.messageInput.isEnabled = !isLoading
        }

        // Observe errors
        viewModel.error.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                viewModel.clearError() // Clear error after showing
            }
        }
    }
}