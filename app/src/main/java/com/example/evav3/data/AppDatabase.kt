package com.example.evav3.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.evav3.data.dao.ConversationDao
import com.example.evav3.data.model.Conversation

/**
 * The Room database for this app.
 * Defines the entities included in the database and provides access to the DAOs.
 */
@Database(
    entities = [Conversation::class], // Add other entities like Message, Memory if needed here
    version = 1,                      // Increment version on schema changes
    exportSchema = false              // Schema export is recommended for complex projects but false for simplicity here
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Provides access to the Data Access Object for Conversation entities.
     */
    abstract fun conversationDao(): ConversationDao

    // Add abstract functions for other DAOs if you create them (e.g., fun messageDao(): MessageDao)
}