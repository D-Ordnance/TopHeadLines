package com.deeosoft.deonews.infrastructure.network

import com.deeosoft.deonews.headlines.data.model.ServerHeadLine
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {
    @GET("top")
    suspend fun getTopHeadLines(
        @Query("locale") country: String = "us",
        @Query("api_token") apiKey: String,
        @Query("limit") limit: String = "3"
    ): ServerHeadLine
}