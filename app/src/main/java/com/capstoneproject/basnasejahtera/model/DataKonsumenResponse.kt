package com.capstoneproject.basnasejahtera.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataKonsumenResponse(

    @field:SerializedName("DataKonsumenResponse")
    val dataKonsumenResponse: List<DataKonsumenResponseItem?>? = null,
) : Parcelable

@Parcelize
data class DataKonsumenResponseItem(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("id_akun")
    val idAkun: Int? = null,

    @field:SerializedName("no_telp")
    val noTelp: String? = null,

    @field:SerializedName("nik")
    val nik: String? = null,

    @field:SerializedName("pekerjaan")
    val pekerjaan: String? = null,

    @field:SerializedName("alamat")
    val alamat: String? = null,

    @field:SerializedName("dataAkun")
    val dataAkun: DataAkunKonsumen? = null,

    @field:SerializedName("dataBooking")
    val dataBooking: DataBooking? = null,
) : Parcelable

@Parcelize
data class DataBooking(
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

    @field:SerializedName("dataRumah")
    val dataRumah: DataRumah? = null,
) : Parcelable

@Parcelize
data class DataRumah(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("id_blok")
    val idBlok: Int? = null,

    @field:SerializedName("tipe_rumah")
    val tipeRumah: String? = null,

    @field:SerializedName("harga")
    val harga: Int? = null,

    @field:SerializedName("progress_pembangunan")
    val progressPembangunan: Int? = null,

    @field:SerializedName("nomor_rumah")
    val nomorRumah: String? = null,
) : Parcelable


