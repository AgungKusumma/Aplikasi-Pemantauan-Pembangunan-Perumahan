package com.capstoneproject.basnasejahtera.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(

	@field:SerializedName("nama")
	val name: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("kata_sandi")
	val kata_sandi: String,

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("isLogin")
	val isLogin: Boolean,

	@field:SerializedName("dataKonsumen")
	val dataKonsumen: DataKonsumen? = null,
) : Parcelable

@Parcelize
data class DataKonsumen(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("id_akun")
	val idAkun: Int? = null,

	@field:SerializedName("nik")
	val nik: String? = null,

	@field:SerializedName("pekerjaan")
	val pekerjaan: String? = null,

	@field:SerializedName("no_telp")
	val noTelp: String? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null,

	) : Parcelable
