package com.dartmedia.chatapp.data

import com.google.gson.annotations.SerializedName

data class ShowChatResponse(

	@field:SerializedName("data")
	val data: DataShowChatResponse? = null
)

data class DataShowChatResponse(

	@field:SerializedName("conversations")
	val conversations: List<ConversationsItemShowChatResponse?>? = null
)

data class ConversationsItemShowChatResponse(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("isSender")
	val isSender: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
