package com.capstoneproject.basnasejahtera.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataRumahResponse(

	@field:SerializedName("DataRumahResponse")
	val dataRumahResponse: List<DataRumahResponseItem>
) : Parcelable

@Parcelize
data class DataRumahResponseItem(
	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("nomorRumah")
	val nomorRumah: String,

	@field:SerializedName("status_rumah")
	val statusRumah: String,

	@field:SerializedName("progress_pembangunan")
	val progressPembangunan: Int,

	@field:SerializedName("harga")
	val harga: Int,

	@field:SerializedName("tipe_rumah")
	val tipeRumah: String,

	@field:SerializedName("id_blok")
	val idBlok: Int,
) : Parcelable
