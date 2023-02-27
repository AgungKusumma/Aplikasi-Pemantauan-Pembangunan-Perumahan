package com.capstoneproject.basnasejahtera.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailDataRumahResponse(

    @field:SerializedName("rumah")
    val rumah: Rumah,
) : Parcelable

@Parcelize
data class Rumah(

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("tipe_rumah")
    val tipeRumah: String,

    @field:SerializedName("harga")
    val harga: Int,

    @field:SerializedName("status_rumah")
    val statusRumah: String,

    @field:SerializedName("progress_pembangunan")
    val progressPembangunan: Int,

    @field:SerializedName("nomorRumah")
    val nomorRumah: String,

    @field:SerializedName("nik")
    val nik: String? = null,

    @field:SerializedName("pekerjaan")
    val pekerjaan: String? = null,

    @field:SerializedName("no_telp")
    val noTelp: String? = null,

    @field:SerializedName("alamat")
    val alamat: String? = null,
) : Parcelable
