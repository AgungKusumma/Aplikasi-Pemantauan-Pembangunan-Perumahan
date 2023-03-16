package com.capstoneproject.basnasejahtera.main.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstoneproject.basnasejahtera.model.*
import com.capstoneproject.basnasejahtera.utils.Event
import com.capstoneproject.basnasejahtera.utils.Injection
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateStatusViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _data = MutableLiveData<Event<DataUpdateResponse>>()
    val data: LiveData<Event<DataUpdateResponse>> = _data

    private val _dataPembangunan = MutableLiveData<Event<DataUpdatePembangunanResponse>>()

    private var _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message

    private var _error = MutableLiveData<Event<Boolean>>()
    val error: LiveData<Event<Boolean>> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun newUpdateStatusPembangunan(
        idRumah: Int,
        photo: MultipartBody.Part,
        persentaseProgress: RequestBody,
        detailProgress: RequestBody,
    ) {
        _isLoading.value = true
        val client = userRepository.newUpdateStatusPembangunan(
            idRumah, photo, persentaseProgress, detailProgress
        )
        client.enqueue(object : Callback<DataUpdatePembangunanResponse> {
            override fun onResponse(
                call: Call<DataUpdatePembangunanResponse>,
                response: Response<DataUpdatePembangunanResponse>,
            ) {
                if (response.isSuccessful) {
                    _error.value = Event(false)
                    userRepository.appExecutors.networkIO.execute {
                        _dataPembangunan.postValue(Event(response.body()!!))
                    }
                } else {
                    Log.e(PEMBANGUNAN, "onResponse fail: ${response.message()}")
                    _message.value = Event(response.message())
                    _error.value = Event(true)
                }
            }

            override fun onFailure(call: Call<DataUpdatePembangunanResponse>, t: Throwable) {
                Log.e(PEMBANGUNAN, "onFailure: " + t.message)
                _message.value = Event(t.message.toString())
                _error.value = Event(true)
            }
        })
    }

    fun updateDataAkun(idAkun: Int, dataUpdateAkun: DataUpdateAkun) {
        val client = userRepository.updateDataAkun(idAkun, dataUpdateAkun)
        client.enqueue(object : Callback<DataUpdateResponse> {
            override fun onResponse(
                call: Call<DataUpdateResponse>,
                response: Response<DataUpdateResponse>,
            ) {
                if (response.isSuccessful) {
                    _error.value = Event(false)
                    userRepository.appExecutors.networkIO.execute {
                        _data.postValue(Event(response.body()!!))
                    }
                } else {
                    Log.e(PEMBANGUNAN, "onResponse fail: ${response.message()}")
                    _message.value = Event(response.message())
                    _error.value = Event(true)
                }
            }

            override fun onFailure(call: Call<DataUpdateResponse>, t: Throwable) {
                Log.e(PEMBANGUNAN, "onFailure: " + t.message)
                _message.value = Event(t.message.toString())
                _error.value = Event(true)
            }
        })
    }

    fun updateDataAkunAdmin(idAkun: Int, dataUpdateAkun: DataUpdateAkunAdmin) {
        val client = userRepository.updateDataAkunAdmin(idAkun, dataUpdateAkun)
        client.enqueue(object : Callback<DataUpdateResponse> {
            override fun onResponse(
                call: Call<DataUpdateResponse>,
                response: Response<DataUpdateResponse>,
            ) {
                if (response.isSuccessful) {
                    _error.value = Event(false)
                    userRepository.appExecutors.networkIO.execute {
                        _data.postValue(Event(response.body()!!))
                    }
                } else {
                    Log.e(PEMBANGUNAN, "onResponse fail: ${response.message()}")
                    _message.value = Event(response.message())
                    _error.value = Event(true)
                }
            }

            override fun onFailure(call: Call<DataUpdateResponse>, t: Throwable) {
                Log.e(PEMBANGUNAN, "onFailure: " + t.message)
                _message.value = Event(t.message.toString())
                _error.value = Event(true)
            }
        })
    }

    fun updateStatusBooking(idRumah: Int, statusBooking: DataUpdateBooking) {
        val client = userRepository.updateStatusBooking(idRumah, statusBooking)
        client.enqueue(object : Callback<DataUpdateResponse> {
            override fun onResponse(
                call: Call<DataUpdateResponse>,
                response: Response<DataUpdateResponse>,
            ) {
                if (response.isSuccessful) {
                    _error.value = Event(false)
                    userRepository.appExecutors.networkIO.execute {
                        _data.postValue(Event(response.body()!!))
                    }
                } else {
                    Log.e(PEMBANGUNAN, "onResponse fail: ${response.message()}")
                    _message.value = Event(response.message())
                    _error.value = Event(true)
                }
            }

            override fun onFailure(call: Call<DataUpdateResponse>, t: Throwable) {
                Log.e(PEMBANGUNAN, "onFailure: " + t.message)
                _message.value = Event(t.message.toString())
                _error.value = Event(true)
            }
        })
    }

    companion object {
        private const val PEMBANGUNAN = "PembangunanViewModel"

        @Volatile
        private var instance: UpdateStatusViewModel? = null

        @JvmStatic
        fun getInstance(context: Context): UpdateStatusViewModel =
            instance ?: synchronized(this) {
                instance ?: UpdateStatusViewModel(
                    Injection.provideRepository(context)
                )
            }.also { instance = it }
    }
}