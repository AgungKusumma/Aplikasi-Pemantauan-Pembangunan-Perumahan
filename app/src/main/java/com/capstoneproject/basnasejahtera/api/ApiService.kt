package com.capstoneproject.basnasejahtera.api

import com.capstoneproject.basnasejahtera.model.DataRumahResponse
import com.capstoneproject.basnasejahtera.model.UserModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
//    @POST("register")
//    fun userRegister(
//        @Body user: Map<String, String>,
//    ): Call<UserModel>

    @POST("login")
    fun userLogin(
        @Body user: Map<String, String>,
    ): Call<UserModel>

    @GET("rumah?namaBlok=a")
    fun getBlokA(): Call<DataRumahResponse>

}