package com.deeosoft.deonews.headlines.di

import android.content.Context
import com.deeosoft.deonews.headlines.data.datasource.HeadLineDataSource
import com.deeosoft.deonews.headlines.data.datasource.HeadLineDataSourceImpl
import com.deeosoft.deonews.headlines.data.repository.HeadLineRepositoryImpl
import com.deeosoft.deonews.headlines.db.HeadLineDatabase
import com.deeosoft.deonews.headlines.domain.repository.HeadLineRepository
import com.deeosoft.deonews.infrastructure.network.NetworkService
import com.deeosoft.deonews.infrastructure.util.Connection
import com.deeosoft.deonews.infrastructure.util.ConnectionImpl
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