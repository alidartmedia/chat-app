package com.dartmedia.chatapp.data

data class SendChatRequest(
    val idReceiver: String,
    val message: String
)
