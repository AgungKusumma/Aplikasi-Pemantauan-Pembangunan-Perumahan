package com.capstoneproject.basnasejahtera.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataAkunResponse(

    @field:SerializedName("data")
    val data: DataAkun? = null,
) : Parcelable

@Parcelize
data class DataAkun(

    @field:SerializedName("seluruhAkun")
    val seluruhAkun: List<SeluruhAkunItem?>? = null,
) : Parcelable

@Parcelize
data class SeluruhAkunItem(

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("role")
    val role: String? = null,

    @field:SerializedName("nama")
    val nama: String? = null,

    @field:SerializedName("dataKonsumen")
    val dataKonsumen: DataKonsumenResponseItem? = null,

    @field:SerializedName("kata_sandi")
    val kataSandi: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("dataBooking")
    val dataBooking: DataBooking? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null,
) : Parcelable

