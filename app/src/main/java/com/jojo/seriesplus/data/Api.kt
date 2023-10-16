package com.jojo.seriesplus.data

import com.jojo.seriesplus.data.models.CastResult
import com.jojo.seriesplus.data.models.ImagesResult
import com.jojo.seriesplus.data.models.Person
import com.jojo.seriesplus.data.models.PersonCastResult
import com.jojo.seriesplus.data.models.SearchResult
import com.jojo.seriesplus.data.models.SeasonsEpisodesResult
import com.jojo.seriesplus.data.models.SeasonsResult
import com.jojo.seriesplus.data.models.Show
import com.jojo.seriesplus.data.models.UpdatesResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("search/shows")
    suspend fun search(@Query("q") term: String): SearchResult

    @GET("shows/{id}")
    suspend fun showDetails(@Path("id") id: Long): Show

    @GET("shows/{id}/seasons")
    suspend fun seasons(@Path("id") id: Long): SeasonsResult

    @GET("seasons/{id}/episodes")
    suspend fun seasonsEpisodes(@Path("id") id: Int): SeasonsEpisodesResult

    @GET("shows/{id}/cast")
    suspend fun cast(@Path("id") id: Long): CastResult

    @GET("shows/{id}/images")
    suspend fun images(@Path("id") id: Long): ImagesResult

    @GET("people/{id}")
    suspend fun person(@Path("id") id: Long): Person

    @GET("people/{id}/castcredits?embed=show")
    suspend fun personCast(@Path("id") id: Long): PersonCastResult

    @GET("updates/shows")
    suspend fun updatesShows(@Query("since") since: String? = null): UpdatesResult

    @GET("updates/people")
    suspend fun updatesPeople(@Query("since") since: String? = null): UpdatesResult
}