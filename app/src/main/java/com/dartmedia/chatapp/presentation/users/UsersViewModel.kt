package com.dartmedia.chatapp.presentation.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dartmedia.chatapp.data.ApiConfig
import com.dartmedia.chatapp.data.ListUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersViewModel: ViewModel() {

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _result = MutableLiveData<ListUserResponse>()
    val result: LiveData<ListUserResponse> get() = _result

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getListUsers(token: String) {
        _isLoading.postValue(true)

        val client = ApiConfig.getApiService().getListUsers("Bearer $token")
        client.enqueue(object : Callback<ListUserResponse>{
            override fun onResponse(
                call: Call<ListUserResponse>,
                response: Response<ListUserResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()?.data != null) {
                        _result.postValue(response.body())
                    } else {
                        _error.postValue("Users is empty")
                    }
                } else {
                    _error.postValue(response.message())
                }
                _isLoading.postValue(false)
            }

            override fun onFailure(call: Call<ListUserResponse>, t: Throwable) {
                _error.postValue(t.message)
                _isLoading.postValue(false)
            }
        })
    }

}