package com.example.evav3.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.evav3.data.model.Conversation
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {

    @Query("SELECT * FROM conversations ORDER BY timestamp DESC") // Query to get all
    fun getAllConversations(): Flow<List<Conversation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE) // Insert or update if exists
    suspend fun insert(conversation: Conversation)

    @Delete
    suspend fun delete(conversation: Conversation)

    // Add other queries as needed (e.g., getById, update, clearAll)
}