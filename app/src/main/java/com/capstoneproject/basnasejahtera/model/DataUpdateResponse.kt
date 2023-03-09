package com.capstoneproject.basnasejahtera.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataUpdateResponse(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("data")
    val data: Data,
) : Parcelable

@Parcelize
data class Data(
    @field:SerializedName("idRumah")
    val idRumah: String,

    @field:SerializedName("statusPembangunan")
    val statusPembangunan: String,
) : Parcelable

@Parcelize
data class DataStatus(
    @field:SerializedName("statusPembangunan")
    val statusPembangunan: Int,
) : Parcelable

@Parcelize
data class DataUpdateBooking(
    @field:SerializedName("idKonsumen")
    val idKonsumen: Int,

    @field:SerializedName("statusBooking")
    val statusBooking: String,

    @field:SerializedName("nominalBooking")
    val nominalBooking: Int,

    @field:SerializedName("tanggalBooking")
    val tanggalBooking: String,
) : Parcelable
