package com.example.evav3.util

import android.content.Context
import android.content.SharedPreferences

object PrefsHelper {
    private const val PREFS_NAME = "eva_prefs"
    private const val KEY_CALL_ACTIVE = "call_active"
    private const val KEY_ACTIVE_SESSION = "active_session"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun setCallActive(context: Context, active: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_CALL_ACTIVE, active).apply()
    }

    fun isCallActive(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_CALL_ACTIVE, false)
    }

    fun setActiveSession(context: Context, sessionId: String) {
        getPrefs(context).edit().putString(KEY_ACTIVE_SESSION, sessionId).apply()
    }

    fun getActiveSession(context: Context): String? {
        return getPrefs(context).getString(KEY_ACTIVE_SESSION, null)
    }
}