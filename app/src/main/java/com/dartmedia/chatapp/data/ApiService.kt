package com.dartmedia.chatapp.data

import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("other-users")
    fun getListUsers(@Header("Authorization") token: String): Call<ListUserResponse>

    @GET("chat/{idChat}")
    fun getChat(@Header("Authorization") token: String, @Path("idChat") idChat: String): Call<ShowChatResponse>

    @POST("chat")
    fun sendChat(@Header("Authorization") token: String, @Body sendChatRequest: SendChatRequest): Call<SendChatResponse>

}