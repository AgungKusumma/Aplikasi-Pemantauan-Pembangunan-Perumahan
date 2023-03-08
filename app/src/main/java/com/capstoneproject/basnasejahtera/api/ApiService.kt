package com.capstoneproject.basnasejahtera.api

import com.capstoneproject.basnasejahtera.model.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("register")
    fun userRegister(
        @Body user: Map<String, String>,
    ): Call<UserModel>

    @POST("login")
    fun userLogin(
        @Body user: Map<String, String>,
    ): Call<UserModel>

    @GET("blok")
    fun getBlok(): Call<List<DataBlokRumahResponseItem>>

    @GET("rumah")
    fun getDataRumah(
        @Query("namaBlok") page: String,
    ): Call<List<DataRumahResponseItem>>

    @GET("rumah/seluruh/data")
    fun getAllDataRumah(): Call<List<DataRumahResponseItem>>

    @GET("rumah/{idRumah}")
    fun getDetailRumah(
        @Path("idRumah") id: Int,
    ): Call<DetailDataRumahResponse>

    @GET("konsumen/rumah/{idKonsumen}")
    fun getDetailRumahKonsumen(
        @Path("idKonsumen") id: Int,
    ): Call<DetailDataRumahResponse>

    @POST("rumah/status-pembangunan/{idRumah}/")
    fun updateStatusPembangunan(
        @Path("idRumah") idRumah: Int,
        @Body statusPembangunan: DataStatus,
    ): Call<DataUpdateResponse>
}