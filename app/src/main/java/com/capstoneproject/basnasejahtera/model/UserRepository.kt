package com.capstoneproject.basnasejahtera.model

import com.capstoneproject.basnasejahtera.api.ApiService
import com.capstoneproject.basnasejahtera.utils.AppExecutors
import retrofit2.Call

class UserRepository private constructor(
    private val apiService: ApiService,
    val appExecutors: AppExecutors,
) {
    fun userLogin(email: String, kataSandi: String): Call<UserModel> {
        val user: Map<String, String> = mapOf(
            "email" to email,
            "kataSandi" to kataSandi
        )

        return apiService.userLogin(user)
    }

//    fun userRegister(name: String, email: String, password: String): Call<FileUploadResponse> {
//        val user: Map<String, String> = mapOf(
//            "name" to name,
//            "email" to email,
//            "password" to password
//        )
//
//        return apiService.userRegister(user)
//    }

    fun getBlok(): Call<List<DataBlokRumahResponseItem>> {
        return apiService.getBlok()
    }

    fun getDataRumah(namaBlok: String): Call<List<DataRumahResponseItem>> {
        return apiService.getDataRumah(namaBlok)
    }

    fun getDetailDataRumah(id: Int): Call<DetailDataRumahResponse> {
        return apiService.getDetailRumah(id)
    }

    fun getDetailDataRumahKonsumen(id: Int): Call<DetailDataRumahResponse> {
        return apiService.getDetailRumahKonsumen(id)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            appExecutors: AppExecutors,
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, appExecutors)
            }.also { instance = it }
    }
}