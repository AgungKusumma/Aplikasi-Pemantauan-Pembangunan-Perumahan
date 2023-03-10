package com.capstoneproject.basnasejahtera.main.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstoneproject.basnasejahtera.model.DetailDataRumahResponse
import com.capstoneproject.basnasejahtera.model.Rumah
import com.capstoneproject.basnasejahtera.model.UserRepository
import com.capstoneproject.basnasejahtera.utils.Event
import com.capstoneproject.basnasejahtera.utils.Injection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailDataViewModel(private val userRepository: UserRepository) : ViewModel() {

    private var _dataRumah = MutableLiveData<Rumah>()
    val dataRumah: LiveData<Rumah> = _dataRumah

    private var _message = MutableLiveData<Event<String>>()

    private var _error = MutableLiveData<Event<Boolean>>()
    val error: LiveData<Event<Boolean>> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getDataRumah(id: Int) {
        _isLoading.value = true
        val client = userRepository.getDetailDataRumah(id)
        client.enqueue(object : Callback<DetailDataRumahResponse> {
            override fun onResponse(
                call: Call<DetailDataRumahResponse>,
                response: Response<DetailDataRumahResponse>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val userResponse = response.body()?.rumah
                    userRepository.appExecutors.networkIO.execute {
                        _dataRumah.postValue(userResponse!!)
                    }
                } else {
                    Log.e(TAG, "onResponse fail: ${response.message()}")
                    _message.value = Event(response.message())
                }
            }

            override fun onFailure(
                call: Call<DetailDataRumahResponse>,
                t: Throwable,
            ) {
                Log.e(TAG, "onFailure: " + t.message)
                _isLoading.value = false
                _message.value = Event(t.message.toString())
            }
        })
    }

    fun getDataRumahKonsumen(id: Int) {
        _isLoading.value = true
        val client = userRepository.getDetailDataRumahKonsumen(id)
        client.enqueue(object : Callback<DetailDataRumahResponse> {
            override fun onResponse(
                call: Call<DetailDataRumahResponse>,
                response: Response<DetailDataRumahResponse>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val userResponse = response.body()?.rumah
                    userRepository.appExecutors.networkIO.execute {
                        _dataRumah.postValue(userResponse!!)
                    }
                } else {
                    Log.e(TAG, "onResponse fail: ${response.message()}")
                    _message.value = Event(response.message())
                }
            }

            override fun onFailure(
                call: Call<DetailDataRumahResponse>,
                t: Throwable,
            ) {
                Log.e(TAG, "onFailure: " + t.message)
                _isLoading.value = false
                _message.value = Event(t.message.toString())
            }
        })
    }

    companion object {
        private const val TAG = "DetailDataViewModel"

        @Volatile
        private var instance: DetailDataViewModel? = null

        @JvmStatic
        fun getInstance(context: Context): DetailDataViewModel =
            instance ?: synchronized(this) {
                instance ?: DetailDataViewModel(
                    Injection.provideRepository(context)
                )
            }.also { instance = it }
    }
}