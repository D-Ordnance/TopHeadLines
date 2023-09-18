package com.deeosoft.pasteltest.headlines.di

import android.content.Context
import com.deeosoft.pasteltest.headlines.data.datasource.HeadLineDataSource
import com.deeosoft.pasteltest.headlines.data.datasource.HeadLineDataSourceImpl
import com.deeosoft.pasteltest.headlines.data.repository.HeadLineRepositoryImpl
import com.deeosoft.pasteltest.headlines.db.HeadLineDatabase
import com.deeosoft.pasteltest.headlines.domain.repository.HeadLineRepository
import com.deeosoft.pasteltest.infrastructure.network.NetworkService
import com.deeosoft.pasteltest.infrastructure.util.Connection
import com.deeosoft.pasteltest.infrastructure.util.ConnectionImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HeadLineModule {
    @Provides
    @Singleton
    fun provideHeadLineDataSource(database: HeadLineDatabase,
                                  networkService: NetworkService,
                                  connection: Connection,
                                  context: Context): HeadLineDataSource =
        HeadLineDataSourceImpl(database, networkService, connection, context)

    @Provides
    @Singleton
    fun provideHeadLineRepository(dataSource: HeadLineDataSource): HeadLineRepository =
        HeadLineRepositoryImpl(dataSource)

    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher =
        Dispatchers.Main
    @Provides
    fun provideNetworkConnectionInfo(context: Context): Connection = ConnectionImpl(context)
}