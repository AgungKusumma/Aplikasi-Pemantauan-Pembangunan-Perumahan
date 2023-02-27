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
) : Parcelable