package com.capstoneproject.basnasejahtera.pengawas

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstoneproject.basnasejahtera.model.DataStatus
import com.capstoneproject.basnasejahtera.model.DataUpdateResponse
import com.capstoneproject.basnasejahtera.model.UserRepository
import com.capstoneproject.basnasejahtera.utils.Event
import com.capstoneproject.basnasejahtera.utils.Injection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateStatusPembangunanViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _data = MutableLiveData<Event<DataUpdateResponse>>()
    val data: LiveData<Event<DataUpdateResponse>> = _data

    private var _message = MutableLiveData<Event<String>>()

    private var _error = MutableLiveData<Event<Boolean>>()
    val error: LiveData<Event<Boolean>> = _error

    fun updateStatusPembangunan(idRumah: Int, statusPembangunan: DataStatus) {
        val client = userRepository.updateStatusPembangunan(idRumah, statusPembangunan)
        client.enqueue(object : Callback<DataUpdateResponse> {
            override fun onResponse(
                call: Call<DataUpdateResponse>,
                response: Response<DataUpdateResponse>,
            ) {
                if (response.isSuccessful) {
                    _error.value = Event(false)
                    userRepository.appExecutors.networkIO.execute {
                        _data.postValue(Event(response.body()!!))
                        Log.e(PEMBANGUNAN, "onResponse success: ${response.body()}")
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
        private var instance: UpdateStatusPembangunanViewModel? = null

        @JvmStatic
        fun getInstance(context: Context): UpdateStatusPembangunanViewModel =
            instance ?: synchronized(this) {
                instance ?: UpdateStatusPembangunanViewModel(
                    Injection.provideRepository(context)
                )
            }.also { instance = it }
    }
}