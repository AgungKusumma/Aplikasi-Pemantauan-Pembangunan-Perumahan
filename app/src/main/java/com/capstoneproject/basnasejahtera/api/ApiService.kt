package com.capstoneproject.basnasejahtera.api

import com.capstoneproject.basnasejahtera.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    @Multipart
    @POST("rumah/progress/{idRumah}")
    fun newUpdateStatusPembangunan(
        @Path("idRumah") idRumah: Int,
        @Part file: MultipartBody.Part,
        @Part("persentaseProgress") persentaseProgress: RequestBody,
        @Part("detailProgress") detailProgress: RequestBody,
    ): Call<DataUpdatePembangunanResponse>

    @POST("rumah/status/{idRumah}/")
    fun updateStatusBooking(
        @Path("idRumah") idRumah: Int,
        @Body statusBooking: DataUpdateBooking,
    ): Call<DataUpdateResponse>

    @PUT("akun/{idAkun}/")
    fun updateDataAkun(
        @Path("idAkun") idAkun: Int,
        @Body dataAkun: DataUpdateAkun,
    ): Call<DataUpdateResponse>

    @GET("kustomer")
    fun getAllDataKonsumen(): Call<List<DataKonsumenResponseItem>>
}