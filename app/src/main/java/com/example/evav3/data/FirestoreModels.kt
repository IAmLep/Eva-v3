package com.example.evav3.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Session(
    @DocumentId val id: String = "",
    val title: String = "",
    val summary: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    val lastSummarizedAt: Timestamp = Timestamp.now()
)

data class Message(
    @DocumentId val id: String = "",
    val role: String = "",     // "user" or "bot"
    val text: String = "",
    val timestamp: Timestamp = Timestamp.now()
)

data class Memory(
    @DocumentId val id: String = "",
    val content: String = "",
    val createdAt: Timestamp = Timestamp.now()
)