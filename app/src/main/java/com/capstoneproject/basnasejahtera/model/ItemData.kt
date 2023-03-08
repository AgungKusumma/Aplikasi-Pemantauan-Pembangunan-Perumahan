package com.capstoneproject.basnasejahtera.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemData(
    var photo: String,
    var data: String,
) : Parcelable