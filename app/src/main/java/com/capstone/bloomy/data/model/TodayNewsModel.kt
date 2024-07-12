package com.capstone.bloomy.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TodayNewsModel (

    val title: String,

    val date: String,

    val imageUrl: String,

    val webUrl: String
) : Parcelable