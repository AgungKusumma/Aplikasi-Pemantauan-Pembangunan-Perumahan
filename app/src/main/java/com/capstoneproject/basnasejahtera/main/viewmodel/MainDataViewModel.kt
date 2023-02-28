package com.capstoneproject.basnasejahtera.main.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstoneproject.basnasejahtera.model.DataBlokRumahResponseItem
import com.capstoneproject.basnasejahtera.model.DataRumahResponseItem
import com.capstoneproject.basnasejahtera.model.UserRepository
import com.capstoneproject.basnasejahtera.utils.Event
import com.capstoneproject.basnasejahtera.utils.Injection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainDataViewModel(private val userRepository: UserRepository) : ViewModel() {

    private var _dataRumah = MutableLiveData<List<DataRumahResponseItem>>()
    val dataRumah: LiveData<List<DataRumahResponseItem>> = _dataRumah

    private var _dataBlok = MutableLiveData<List<DataBlokRumahResponseItem>>()
    val dataBlok: LiveData<List<DataBlokRumahResponseItem>> = _dataBlok

    private var _message = MutableLiveData<Event<String>>()

    private var _error = MutableLiveData<Event<Boolean>>()
    val error: LiveData<Event<Boolean>> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getDataRumah(namaBlok: String) {
        _isLoading.value = true
        val client = userRepository.getDataRumah(namaBlok)
        client.enqueue(object : Callback<List<DataRumahResponseItem>> {
            override fun onResponse(
                call: Call<List<DataRumahResponseItem>>,
                response: Response<List<DataRumahResponseItem>>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    userRepository.appExecutors.networkIO.execute {
                        _dataRumah.postValue(response.body()!!)
                    }
                } else {
                    Log.e(TAG, "onResponse fail: ${response.message()}")
                    _message.value = Event(response.message())
                }
            }

            override fun onFailure(
                call: Call<List<DataRumahResponseItem>>,
                t: Throwable,
            ) {
                Log.e(TAG, "onFailure: " + t.message)
                _isLoading.value = false
                _message.value = Event(t.message.toString())
            }
        })
    }

    fun getBlok() {
        _isLoading.value = true
        val client = userRepository.getBlok()
        client.enqueue(object : Callback<List<DataBlokRumahResponseItem>> {
            override fun onResponse(
                call: Call<List<DataBlokRumahResponseItem>>,
                response: Response<List<DataBlokRumahResponseItem>>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    userRepository.appExecutors.networkIO.execute {
                        _dataBlok.postValue(response.body()!!)
                    }
                } else {
                    Log.e(TAG, "onResponse fail: ${response.message()}")
                    _message.value = Event(response.message())
                }
            }

            override fun onFailure(
                call: Call<List<DataBlokRumahResponseItem>>,
                t: Throwable,
            ) {
                Log.e(TAG, "onFailure: " + t.message)
                _isLoading.value = false
                _message.value = Event(t.message.toString())
            }
        })
    }

    companion object {
        private const val TAG = "MainDataViewModel"

        @Volatile
        private var instance: MainDataViewModel? = null

        @JvmStatic
        fun getInstance(context: Context): MainDataViewModel =
            instance ?: synchronized(this) {
                instance ?: MainDataViewModel(
                    Injection.provideRepository(context)
                )
            }.also { instance = it }
    }
}