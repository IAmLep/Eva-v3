package com.example.evav3.util // Replace with your actual package name

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * Utility for accessing securely encrypted SharedPreferences.
 */
object SecureStorage {

    private const val PREF_FILE_NAME = "eva_v3_secure_prefs"

    // Define keys for stored values
    const val KEY_SERVICE_ACCOUNT_JSON = "service_account_json"
    const val KEY_ACTIVE_SESSION_ID = "active_session_id"
    const val KEY_CALL_STATE = "call_state" // e.g., "IDLE", "ACTIVE"
    // Add other keys as needed

    @Volatile private var sharedPreferences: SharedPreferences? = null

    fun getInstance(context: Context): SharedPreferences {
        return sharedPreferences ?: synchronized(this) {
            sharedPreferences ?: createEncryptedPrefs(context).also { sharedPreferences = it }
        }
    }

    private fun createEncryptedPrefs(context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            PREF_FILE_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    // --- Convenience Methods (Optional) ---

    fun saveString(context: Context, key: String, value: String?) {
        getInstance(context).edit().putString(key, value).apply()
    }

    fun getString(context: Context, key: String, defaultValue: String? = null): String? {
        return getInstance(context).getString(key, defaultValue)
    }

    // Add methods for other types (Boolean, Int, etc.) if needed
}