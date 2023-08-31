package com.dartmedia.chatapp.presentation.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dartmedia.chatapp.data.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatViewModel: ViewModel() {

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _result = MutableLiveData<ShowChatResponse>()
    val result: LiveData<ShowChatResponse> get() = _result

    private val _resultSendChat = MutableLiveData<SendChatResponse>()
    val resultSendChat: LiveData<SendChatResponse> get() = _resultSendChat

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun showChat(token: String, idReceiver: String) {
        val client = ApiConfig.getApiService().getChat("Bearer $token", idReceiver)
        client.enqueue(object : Callback<ShowChatResponse> {
            override fun onResponse(
                call: Call<ShowChatResponse>,
                response: Response<ShowChatResponse>
            ) {
                if (response.isSuccessful) {
                    _result.postValue(response.body())
                } else {
                    _error.postValue(response.message())
                }
            }

            override fun onFailure(call: Call<ShowChatResponse>, t: Throwable) {
                _error.postValue(t.message)
            }
        })
    }

    fun sendChat(token: String, idReceiver: String, message: String) {
        _isLoading.postValue(true)

        val client = ApiConfig.getApiService().sendChat("Bearer $token", SendChatRequest(idReceiver, message))
        client.enqueue(object : Callback<SendChatResponse> {
            override fun onResponse(
                call: Call<SendChatResponse>,
                response: Response<SendChatResponse>
            ) {
                if (response.isSuccessful) {
                    _resultSendChat.postValue(response.body())
                } else {
                    _error.postValue(response.message())
                }
                _isLoading.postValue(false)
            }

            override fun onFailure(call: Call<SendChatResponse>, t: Throwable) {
                _isLoading.postValue(false)
                _error.postValue(t.message)
            }
        })
    }

}