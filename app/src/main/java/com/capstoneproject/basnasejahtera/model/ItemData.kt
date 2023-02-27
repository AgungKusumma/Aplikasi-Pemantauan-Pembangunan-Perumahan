package com.capstoneproject.basnasejahtera.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemData(
    var photo: String,
    var id: String,
    var info: String,
    var nama: String,
    var status: Int
) : Parcelable

