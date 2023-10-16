package com.jojo.seriesplus.data.models

import com.google.gson.annotations.SerializedName

data class Person(
    val birthday: String?,
    val country: Country?,
    val deathday: String?,
    val gender: String,
    val id: Long,
    val image: Image?,
    @SerializedName("_links")
    val links: Links,
    val name: String,
    val updated: Int,
    val url: String
)