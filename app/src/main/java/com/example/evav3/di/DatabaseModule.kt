package com.example.evav3.di

import android.content.Context
import androidx.room.Room
import com.example.evav3.data.AppDatabase
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
            "eva_database"
        ).fallbackToDestructiveMigration() // Added fallback for simplicity during dev
            .build()
    }

    // REMOVE THIS ENTIRE FUNCTION:
    /*
    @Provides
    @Singleton // Providing the DAO explicitly is usually not needed when injecting it directly
    fun provideConversationDao(database: AppDatabase): ConversationDao {
        return database.conversationDao()
    }
    */
}