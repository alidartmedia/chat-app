package com.dartmedia.chatapp.data

import com.google.gson.annotations.SerializedName

data class ListUserResponse(

	@field:SerializedName("data")
	val data: DataListUserResponse? = null,

	@field:SerializedName("idUser")
	val idUser: String? = null
)

data class UsersItemListUserResponse(

	@field:SerializedName("idUser")
	val idUser: String? = null,

	@field:SerializedName("noTelpUser")
	val noTelpUser: String? = null,

	@field:SerializedName("nameUser")
	val nameUser: String? = null
)

data class DataListUserResponse(

	@field:SerializedName("users")
	val users: List<UsersItemListUserResponse?>? = null
)
