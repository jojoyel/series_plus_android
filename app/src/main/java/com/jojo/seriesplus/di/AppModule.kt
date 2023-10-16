package com.jojo.seriesplus.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.jojo.seriesplus.BuildConfig
import com.jojo.seriesplus.data.Api
import com.jojo.seriesplus.data.db.HistoryRepository
import com.jojo.seriesplus.data.db.HistoryRepositoryImpl
import com.jojo.seriesplus.data.db.SeriesPlusDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesApi(): Api {
        val okHttp = OkHttpClient.Builder()
        if (BuildConfig.DEBUG)
            okHttp.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))

        okHttp.addInterceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)

            if (response.code !in 200..299)
                Log.d("Series Plus Errors", "providesApi: Ouch")

            return@addInterceptor response
        }

        return Retrofit.Builder()
            .client(okHttp.build())
            .baseUrl(BuildConfig.TVMAZE_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }

    @Provides
    @Singleton
    fun providesDb(@ApplicationContext context: Context): SeriesPlusDatabase =
        Room.databaseBuilder(
            context,
            SeriesPlusDatabase::class.java,
            SeriesPlusDatabase.DATABASE_NAME
        ).build()

    @Provides
    @Singleton
    fun providesHistoryRepository(db: SeriesPlusDatabase): HistoryRepository =
        HistoryRepositoryImpl(db.historyDao)
}