package com.deeosoft.pasteltest.headlines.repository

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import com.deeosoft.pasteltest.BuildConfig
import com.deeosoft.pasteltest.R
import com.deeosoft.pasteltest.util.MockablePastel
import com.deeosoft.pasteltest.headlines.db.HeadLineDatabase
import com.deeosoft.pasteltest.headlines.db.model.HeadLineItem
import com.deeosoft.pasteltest.headlines.db.model.UIHeadLinesCollection
import com.deeosoft.pasteltest.infrastructure.network.NetworkService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@MockablePastel
class HeadLinesRepository
@Inject constructor(
    private val networkService: NetworkService,
    private val database: HeadLineDatabase,
    app: Application
){
    private val context = app.applicationContext
    private suspend fun remoteSource(): Resource<UIHeadLinesCollection> =
        try {
            val response = networkService.getTopHeadLines(apiKey = BuildConfig.API_KEY)
            if(response.status.equals("ok", ignoreCase = true)) {
                saveToLocal(response.articles)
                localSource()
            }
            else {
                Resource.Error(response.status)
            }

        }catch (ex: Exception){
            ex.printStackTrace()
            Resource.Error(ex.message!!)
        }

    private suspend fun localSource(): Resource<UIHeadLinesCollection> =
        try {
            val response = database.headLineDao().getTopHeadLines()
            Resource.Success(UIHeadLinesCollection(response))
        }catch (ex: Exception){
            if(ex is SQLiteConstraintException){
                Resource.Error(context.getString(R.string.no_new_update))
            }else{
                Resource.Error(context.getString(R.string.default_error_message))
            }
        }

    private suspend fun saveToLocal(data: List<HeadLineItem?>){
        database.headLineDao().insert(data.asReversed())
    }

    suspend fun getTopHeadLines(forceServer: Boolean): Flow<Resource<UIHeadLinesCollection>> =
        flow{
            val response: Resource<UIHeadLinesCollection>?
            if (!forceServer) {
                response = localSource()
                emit(response)
            }
            val remoteResponse = remoteSource()
            remoteResponse.data?.let { saveToLocal(it.articles) }
            emit(remoteResponse)
        }
}

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
){
    class Success<T>(data: T?): Resource<T>(data)
    class Error<T>(message: String, data: T? = null): Resource<T>(message = message)
}