package com.dartmedia.chatapp.data

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("data")
	val data: DataLoginResponse? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataLoginResponse(

	@field:SerializedName("accessToken")
	val accessToken: String? = null
)
