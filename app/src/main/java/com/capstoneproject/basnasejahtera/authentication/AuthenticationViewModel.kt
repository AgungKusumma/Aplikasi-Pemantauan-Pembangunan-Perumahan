package com.capstoneproject.basnasejahtera.authentication

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstoneproject.basnasejahtera.model.UserModel
import com.capstoneproject.basnasejahtera.model.UserRepository
import com.capstoneproject.basnasejahtera.utils.Event
import com.capstoneproject.basnasejahtera.utils.Injection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthenticationViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _user = MutableLiveData<Event<UserModel>>()
    val user: LiveData<Event<UserModel>> = _user

    private var _message = MutableLiveData<Event<String>>()

    private var _error = MutableLiveData<Event<Boolean>>()
    val error: LiveData<Event<Boolean>> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun userLogin(email: String, kataSandi: String) {
        _isLoading.value = true
        val client = userRepository.userLogin(email, kataSandi)
        client.enqueue(object : Callback<UserModel> {
            override fun onResponse(
                call: Call<UserModel>,
                response: Response<UserModel>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _error.value = Event(false)
                    userRepository.appExecutors.networkIO.execute {
                        _user.postValue(Event(response.body()!!))
                    }
                } else {
                    Log.e(LOGIN, "onResponse fail: ${response.message()}")
                    _message.value = Event(response.message())
                    _error.value = Event(true)
                }
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                Log.e(LOGIN, "onFailure: " + t.message)
                _isLoading.value = false
                _message.value = Event(t.message.toString())
                _error.value = Event(true)
            }
        })
    }

//    fun userRegister(name: String, email: String, password: String) {
//        val client = userRepository.userRegister(name, email, password)
//        client.enqueue(object : Callback<FileUploadResponse> {
//            override fun onResponse(
//                call: Call<FileUploadResponse>,
//                response: Response<FileUploadResponse>,
//            ) {
//                Log.e(SIGNUP, "onResponse: " + response.body())
//                if (response.isSuccessful) {
//                    _error.value = Event(false)
//                } else {
//                    Log.e(SIGNUP, "onResponse fail: ")
//                    _message.value = Event(response.message())
//                    _error.value = Event(true)
//                }
//            }
//
//            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
//                Log.e(SIGNUP, "onFailure: " + t.message)
//                _message.value = Event(t.message.toString())
//                _error.value = Event(true)
//            }
//        })
//    }

    companion object {
        private const val LOGIN = "LoginViewModel"
        private const val SIGNUP = "SignupViewModel"

        @Volatile
        private var instance: AuthenticationViewModel? = null

        @JvmStatic
        fun getInstance(context: Context): AuthenticationViewModel =
            instance ?: synchronized(this) {
                instance ?: AuthenticationViewModel(
                    Injection.provideRepository(context)
                )
            }.also { instance = it }
    }
}