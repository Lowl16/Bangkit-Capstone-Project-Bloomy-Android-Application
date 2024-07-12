package com.capstone.bloomy.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TopNewsModel (

    val title: String,

    val imageUrl: String,

    val webUrl: String
) : Parcelable