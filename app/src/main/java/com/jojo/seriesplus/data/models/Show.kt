package com.jojo.seriesplus.data.models

import com.google.gson.annotations.SerializedName

data class Show(
    val averageRuntime: Int?,
    val dvdCountry: Any?,
    val ended: String?,
    val externals: Externals,
    val genres: List<String>,
    val id: Long,
    val image: Image?,
    val language: String,
    @SerializedName("_links")
    val links: Links,
    val name: String,
    val network: Network?,
    val officialSite: String?,
    val premiered: String?,
    val rating: Rating,
    val runtime: Int?,
    val schedule: Schedule,
    val status: String,
    val summary: String?,
    val type: String,
    val updated: Int,
    val url: String,
    val webChannel: WebChannel?,
    val weight: Int
)