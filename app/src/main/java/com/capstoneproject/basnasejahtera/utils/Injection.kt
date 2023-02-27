package com.capstoneproject.basnasejahtera.utils

import android.content.Context
import com.capstoneproject.basnasejahtera.api.ApiConfig
import com.capstoneproject.basnasejahtera.model.UserRepository

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig().getApiService()
        val appExecutors = AppExecutors()
        return UserRepository.getInstance(apiService, appExecutors)
    }
}