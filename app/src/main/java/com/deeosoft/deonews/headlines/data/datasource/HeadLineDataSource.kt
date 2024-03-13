package com.deeosoft.deonews.headlines.data.datasource

import com.deeosoft.deonews.headlines.data.model.UIHeadLinesCollection
import com.deeosoft.deonews.headlines.domain.repository.Resource

interface HeadLineDataSource {
    suspend fun remoteSource(): Resource<UIHeadLinesCollection>
    suspend fun localSource(): Resource<UIHeadLinesCollection>
}