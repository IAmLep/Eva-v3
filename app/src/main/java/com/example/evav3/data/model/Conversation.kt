package com.example.evav3.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversations") // Define table name for Room
data class Conversation(
    @PrimaryKey(autoGenerate = true) // Auto-generate IDs
    val id: Long = 0,
    val title: String, // Example field
    val lastMessage: String, // Example field
    val timestamp: Long // Example field
    // Add other relevant fields
)