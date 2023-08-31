package com.dartmedia.chatapp.data

data class ChatMQTTResponse(
    val idSender: String,
    val idReceiver: String,
    val message: String
)
