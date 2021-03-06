package com.example.letstalk.model

data class Messages(
    val senderId: String,
    val message: String,
    val messageId: String,
    val timeStamp: Long
)