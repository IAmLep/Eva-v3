package com.example.evav3.di

import android.content.Context
import androidx.room.Room
import com.example.evav3.data.AppDatabase // Correct import based on AppDatabase.kt package
import com.example.evav3.data.dao.ConversationDao // <<< IMPORT ADDED
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "eva_database" // Database file name
        ).fallbackToDestructiveMigration() // Consider removing for production
            .build()
    }

    // --- FUNCTION UNCOMMENTED ---
    @Provides
    @Singleton // Providing the DAO explicitly
    fun provideConversationDao(database: AppDatabase): ConversationDao {
        return database.conversationDao()
    }
    // --- END OF UNCOMMENT ---
}