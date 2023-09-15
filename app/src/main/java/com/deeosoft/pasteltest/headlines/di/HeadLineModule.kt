package com.deeosoft.pasteltest.headlines.di

import android.content.Context
import com.deeosoft.pasteltest.headlines.data.datasource.HeadLineDataSource
import com.deeosoft.pasteltest.headlines.data.datasource.HeadLineDataSourceImpl
import com.deeosoft.pasteltest.headlines.data.repository.HeadLineRepositoryImpl
import com.deeosoft.pasteltest.headlines.db.HeadLineDatabase
import com.deeosoft.pasteltest.headlines.domain.repository.HeadLineRepository
import com.deeosoft.pasteltest.infrastructure.network.NetworkService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HeadLineModule {
    @Provides
    @Singleton
    fun provideHeadLineDataSource(database: HeadLineDatabase,
                                  networkService: NetworkService,
                                  context: Context): HeadLineDataSource =
        HeadLineDataSourceImpl(database, networkService, context)

    @Provides
    @Singleton
    fun provideHeadLineRepository(dataSource: HeadLineDataSource): HeadLineRepository =
        HeadLineRepositoryImpl(dataSource)
}