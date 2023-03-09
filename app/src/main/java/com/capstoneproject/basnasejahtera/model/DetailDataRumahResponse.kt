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

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null,

    @field:SerializedName("dataAkunKonsumen")
    val dataAkunKonsumen: DataAkunKonsumen? = null,

    @field:SerializedName("dataKonsumen")
    val dataKonsumen: DataKonsumenNew? = null,

    @field:SerializedName("dataBooking")
    val dataBooking: DataBookingRumah? = null,
) : Parcelable

@Parcelize
data class DataAkunKonsumen(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("kata_sandi")
    val kataSandi: String? = null,

    @field:SerializedName("nama")
    val nama: String? = null,

    @field:SerializedName("role")
    val role: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null,
) : Parcelable

@Parcelize
data class DataKonsumenNew(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("id_akun")
    val idAkun: Int? = null,

    @field:SerializedName("no_telp")
    val noTelp: String? = null,

    @field:SerializedName("nik")
    val nik: String? = null,

    @field:SerializedName("alamat")
    val alamat: String? = null,

    @field:SerializedName("pekerjaan")
    val pekerjaan: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null,
) : Parcelable

@Parcelize
data class DataBookingRumah(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("id_konsumen")
    val idKonsumen: Int? = null,

    @field:SerializedName("id_rumah")
    val idRumah: Int? = null,

    @field:SerializedName("status_booking")
    val statusBooking: String? = null,

    @field:SerializedName("nominal_booking")
    val nominalBooking: Int? = null,

    @field:SerializedName("tanggal_booking")
    val tanggalBooking: String? = null,
) : Parcelable
