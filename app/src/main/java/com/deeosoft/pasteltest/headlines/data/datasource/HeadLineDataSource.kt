package com.deeosoft.pasteltest.headlines.data.datasource

import com.deeosoft.pasteltest.headlines.data.model.UIHeadLinesCollection
import com.deeosoft.pasteltest.headlines.domain.repository.Resource

interface HeadLineDataSource {
    suspend fun remoteSource(): Resource<UIHeadLinesCollection>
    suspend fun localSource(): Resource<UIHeadLinesCollection>
}