package com.example.evav3 // Replace with your actual package name

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestoreSettings
import dagger.hilt.android.HiltAndroidApp // Import Hilt annotation

/**
 * Custom Application class for initializing Firebase, Hilt, and other global components.
 */
@HiltAndroidApp // Add this annotation for Hilt
class EvaApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Optional: Configure Firestore settings (e.g., persistence, caching)
        val settings = firestoreSettings {
            isPersistenceEnabled = true // Enable offline persistence
            // cacheSizeBytes = FirebaseFirestore.CACHE_SIZE_UNLIMITED // Optional: Adjust cache size
        }
        FirebaseFirestore.getInstance().firestoreSettings = settings

        // Hilt setup is handled by the annotation.
        // Initialize other singletons or libraries here if needed
    }
}