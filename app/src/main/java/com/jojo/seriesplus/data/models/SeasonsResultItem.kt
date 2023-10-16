package com.jojo.seriesplus.data.models

import com.google.gson.annotations.SerializedName

data class SeasonsResultItem(
    val endDate: String,
    val episodeOrder: Int,
    val id: Int,
    val image: Image?,
    @SerializedName("_links")
    val links: Links,
    var name: String,
    val network: Network,
    val number: Int,
    val premiereDate: String,
    val summary: String,
    val url: String,
    val webChannel: Any?
)