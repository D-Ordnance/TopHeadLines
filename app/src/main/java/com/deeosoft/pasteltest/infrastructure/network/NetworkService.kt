package com.deeosoft.pasteltest.infrastructure.network

import com.deeosoft.pasteltest.headlines.db.model.ServerHeadLine
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {
    @GET("top-headlines")
    suspend fun getTopHeadLines(
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String
    ): ServerHeadLine
}