package com.jojo.seriesplus.data.models


import com.google.gson.annotations.SerializedName

data class PersonCastResultItem(
    @SerializedName("_embedded")
    val embedded: Embedded,
    @SerializedName("_links")
    val links: LinksCharacter,
    val self: Boolean,
    val voice: Boolean
)