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
data class DataUpdatePembangunanResponse(
    @field:SerializedName("id_blok")
    val idBlok: Int? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("nomor_rumah")
    val nomorRumah: String? = null,

    @field:SerializedName("harga")
    val harga: Int? = null,

    @field:SerializedName("image_progress_pembangunan")
    val imageProgressPembangunan: String? = null,

    @field:SerializedName("details_progress_pembangunan")
    val detailsProgressPembangunan: String? = null,

    @field:SerializedName("tipe_rumah")
    val tipeRumah: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("progress_pembangunan")
    val progressPembangunan: String? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null,
) : Parcelable

@Parcelize
data class Data(
    @field:SerializedName("idRumah")
    val idRumah: String,

    @field:SerializedName("statusPembangunan")
    val statusPembangunan: String,
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

@Parcelize
data class DataUpdateAkun(
    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("kataSandi")
    val kataSandi: String,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("noHp")
    val noHp: String,

    @field:SerializedName("nik")
    val nik: String,

    @field:SerializedName("pekerjaan")
    val pekerjaan: String,

    @field:SerializedName("alamat")
    val alamat: String,
) : Parcelable

@Parcelize
data class DataUpdateAkunAdmin(
    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("kataSandi")
    val kataSandi: String,

    @field:SerializedName("nama")
    val nama: String,
) : Parcelable
