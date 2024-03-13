package com.deeosoft.deonews.headlines.data.repository

import com.deeosoft.deonews.headlines.data.datasource.HeadLineDataSource
import com.deeosoft.deonews.headlines.data.model.UIHeadLinesCollection
import com.deeosoft.deonews.headlines.domain.repository.HeadLineRepository
import com.deeosoft.deonews.headlines.domain.repository.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HeadLineRepositoryImpl
@Inject constructor(
    private val dataSource: HeadLineDataSource,
) : HeadLineRepository {
    override suspend fun getTopHeadLines(forceServer: Boolean): Flow<Resource<UIHeadLinesCollection>> =
        flow {
            val response: Resource<UIHeadLinesCollection>?
            if (!forceServer) {
                response = dataSource.localSource()
                emit(response)
            }
            val remoteResponse: Resource<UIHeadLinesCollection>?
            remoteResponse = dataSource.remoteSource()
            emit(remoteResponse)
        }
}