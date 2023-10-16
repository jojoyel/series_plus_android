package com.jojo.seriesplus.data.models


import com.google.gson.annotations.SerializedName

data class SeasonsEpisodesResultItem(
    val airdate: String,
    val airstamp: String,
    val airtime: String,
    val id: Int,
    val image: Image?,
    @SerializedName("_links")
    val links: LinksSeasonEpisode,
    val name: String,
    val number: Int,
    val rating: Rating,
    val runtime: Int,
    val season: Int,
    val summary: String?,
    val type: String,
    val url: String
)