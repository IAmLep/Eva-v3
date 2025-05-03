package com.example.evav3.di

import android.content.Context
import androidx.room.Room
import com.example.evav3.data.AppDatabase
import com.example.evav3.data.dao.ConversationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing database-related dependencies (AppDatabase, DAOs).
 */
@Module
@InstallIn(SingletonComponent::class) // Scope dependencies to the application lifecycle
object DatabaseModule {

    private const val DATABASE_NAME = "eva_database" // Constant for the database file name

    /**
     * Provides a singleton instance of the AppDatabase.
     * @param appContext The application context provided by Hilt.
     * @return The singleton AppDatabase instance.
     */
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            DATABASE_NAME
        )
            // In development, you might add .fallbackToDestructiveMigration()
            // to avoid crashes during schema changes, but this deletes all data.
            // For production, implement proper migrations: .addMigrations(MIGRATION_1_2, ...)
            .build()
    }

    /**
     * Provides a singleton instance of the ConversationDao using the AppDatabase.
     * @param appDatabase The AppDatabase instance provided by Hilt.
     * @return The singleton ConversationDao instance.
     */
    @Provides
    @Singleton
    fun provideConversationDao(appDatabase: AppDatabase): ConversationDao {
        return appDatabase.conversationDao()
    }

    // Add @Provides methods here for any other DAOs defined in AppDatabase
    // e.g., @Provides @Singleton fun provideMessageDao(appDatabase: AppDatabase): MessageDao = appDatabase.messageDao()
}