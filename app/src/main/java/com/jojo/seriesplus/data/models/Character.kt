package com.jojo.seriesplus.data.models

import com.google.gson.annotations.SerializedName

data class Character(
    val id: Int,
    val image: Image?,
    @SerializedName("_links")
    val links: Links,
    val name: String,
    val url: String
)