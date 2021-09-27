package com.example.podlodkaandroidcrew.api

import com.example.podlodkaandroidcrew.data.model.Session
import com.example.podlodkaandroidcrew.data.model.SessionResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface PodlodkaAndroidCrewApiService {
    @GET("AJIEKCX/901e7ae9593e4afd136abe10ca7d510f/raw/61e7c1f037345370cf28b5ae6fdaffdd9e7e18d5/Sessions.json")
    suspend fun getSessions(): List<SessionResponse>
}

object PodlodkaAndroidCrewApi {
    private const val BASE_URL = "https://gist.githubusercontent.com"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    val retrofitService: PodlodkaAndroidCrewApiService by lazy {
        retrofit.create(PodlodkaAndroidCrewApiService::class.java)
    }
}