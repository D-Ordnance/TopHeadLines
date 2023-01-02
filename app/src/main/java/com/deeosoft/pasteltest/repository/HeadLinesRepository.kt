package com.deeosoft.pasteltest.repository

import com.deeosoft.pasteltest.BuildConfig
import com.deeosoft.pasteltest.db.HeadLineDatabase
import com.deeosoft.pasteltest.db.model.HeadLineItem
import com.deeosoft.pasteltest.db.model.UIHeadLinesCollection
import com.deeosoft.pasteltest.network.NetworkService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HeadLinesRepository
@Inject constructor(
    private val networkService: NetworkService,
    private val database: HeadLineDatabase
){
    private suspend fun remoteSource(localData: Resource<UIHeadLinesCollection>): Resource<UIHeadLinesCollection> =
        try {
            val response = networkService.getTopHeadLines(apiKey = BuildConfig.API_KEY)
            if(response.status.equals("ok", ignoreCase = true))
                Resource.Success(UIHeadLinesCollection(response.articles))
            else {
                Resource.Error(response.status, localData.data)
            }

        }catch (ex: Exception){
            ex.printStackTrace()
            Resource.Error(ex.message!!, localData.data)
        }

    private suspend fun localSource(): Resource<UIHeadLinesCollection> =
        try {
            val response = database.headLineDao().getTopHeadLines()
            Resource.Success(UIHeadLinesCollection(response))
        }catch (ex: Exception){
            remoteSource(Resource.Error(message = "An error occurred"))
        }

    private suspend fun saveToLocal(data: List<HeadLineItem?>){
        database.headLineDao().insert(data)
    }

    suspend fun getTopHeadLines(forceServer: Boolean): Flow<Resource<UIHeadLinesCollection>> =
        flow{
            var response: Resource<UIHeadLinesCollection>? = null
            if (!forceServer) {
                response = localSource()
                emit(response)
            }
            val remoteResponse = response?.let { remoteSource(localData = it) }
            if (remoteResponse != null) {
                remoteResponse.data?.let { saveToLocal(it.articles) }
                emit(remoteResponse)
            }
        }
}

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
){
    class Success<T>(data: T?): Resource<T>(data)
    class Error<T>(message: String, data: T? = null): Resource<T>(message = message)
}