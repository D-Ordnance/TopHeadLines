package com.deeosoft.deonews.headlines.data.datasource

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import com.deeosoft.deonews.BuildConfig
import com.deeosoft.deonews.R
import com.deeosoft.deonews.headlines.data.model.HeadLineItem
import com.deeosoft.deonews.headlines.data.model.UIHeadLinesCollection
import com.deeosoft.deonews.headlines.db.HeadLineDatabase
import com.deeosoft.deonews.headlines.domain.repository.Resource
import com.deeosoft.deonews.infrastructure.network.NetworkService
import com.deeosoft.deonews.infrastructure.util.Connection
import javax.inject.Inject

class HeadLineDataSourceImpl @Inject constructor(
    private val database: HeadLineDatabase,
    private val networkService: NetworkService,
    private val connection: Connection,
    private val context: Context
): HeadLineDataSource {
    override suspend fun remoteSource(): Resource<UIHeadLinesCollection> =
        if(connection.isNetworkAvailable()) {
            try {
                val response = networkService.getTopHeadLines(apiKey = BuildConfig.API_KEY)
                if(response.error == null){
                    saveToLocal(response.data)
                    localSource()
                }else{
                    Resource.Error(response.error.message)
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                Resource.Error(ex.message!!)
            }
        }else{
            Resource.Error("No Internet Connection")
        }

    override suspend fun localSource(): Resource<UIHeadLinesCollection> =
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
}