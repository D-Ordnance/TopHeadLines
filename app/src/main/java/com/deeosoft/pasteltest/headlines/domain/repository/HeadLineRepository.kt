package com.deeosoft.pasteltest.headlines.domain.repository

import com.deeosoft.pasteltest.headlines.data.model.UIHeadLinesCollection
import kotlinx.coroutines.flow.Flow

interface HeadLineRepository {
    suspend fun getTopHeadLines(forceServer: Boolean): Flow<Resource<UIHeadLinesCollection>>
}

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
){
    class Success<T>(data: T?): Resource<T>(data)
    class Error<T>(message: String, data: T? = null): Resource<T>(message = message)
}