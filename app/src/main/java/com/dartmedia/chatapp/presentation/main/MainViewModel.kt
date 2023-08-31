package com.dartmedia.chatapp.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dartmedia.chatapp.data.ApiConfig
import com.dartmedia.chatapp.data.LoginRequest
import com.dartmedia.chatapp.data.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _result = MutableLiveData<LoginResponse>()
    val result: LiveData<LoginResponse> get() = _result

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun login(no: String) {
        _isLoading.postValue(true)

        val client = ApiConfig.getApiService().login(LoginRequest(no))
        client.enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        _result.postValue(response.body())
                    } else {
                        _error.postValue("Login gagal")
                    }
                } else {
                    _error.postValue(response.message())
                }
                _isLoading.postValue(false)
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _error.postValue(t.message)
                _isLoading.postValue(false)
            }
        })
    }

}