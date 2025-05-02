package com.example.evav3.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    /**
     * Gets the current user's Google ID token.
     * Returns null if no user is signed in or if token fetch fails.
     */
    suspend fun getUserIdToken(forceRefresh: Boolean = false): String? {
        val user = getCurrentUser() ?: run {
            Timber.w("getUserIdToken called but no user is signed in.")
            return null
        }
        return try {
            val tokenResult = user.getIdToken(forceRefresh).await()
            tokenResult?.token?.also {
                Timber.d("Successfully fetched ID token.")
            } ?: run {
                Timber.e("getIdToken() returned null result.")
                null
            }
        } catch (e: Exception) {
            Timber.e(e, "Error getting ID token")
            // Specific handling might be needed e.g. FirebaseTooManyRequestsException
            null
        }
    }

    // No signOut needed for purely anonymous flow unless you want to reset the anonymous ID
}