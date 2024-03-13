package com.deeosoft.deonews.headlines.data.datasource

import android.content.Context
import com.deeosoft.deonews.BuildConfig
import com.deeosoft.deonews.headlines.data.model.ServerError
import com.deeosoft.deonews.headlines.data.model.ServerHeadLine
import com.deeosoft.deonews.headlines.data.model.UIHeadLinesCollection
import com.deeosoft.deonews.headlines.data.repository.HeadLinesRepositoryImplTest
import com.deeosoft.deonews.headlines.db.HeadLineDao
import com.deeosoft.deonews.headlines.db.HeadLineDatabase
import com.deeosoft.deonews.headlines.domain.repository.Resource
import com.deeosoft.deonews.infrastructure.network.NetworkService
import com.deeosoft.deonews.infrastructure.util.Connection
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verifyNoInteractions

@OptIn(ExperimentalCoroutinesApi::class)
class HeadLineDataSourceImplTest {
    @Mock
    private lateinit var connection: Connection
    @Mock
    private lateinit var networkService: NetworkService
    @Mock
    private lateinit var database: HeadLineDatabase
    @Mock
    private lateinit var headLineDao: HeadLineDao
    @Mock
    private lateinit var context: Context

    private lateinit var dataSource: HeadLineDataSource

    private val successfulServerHeadLine = ServerHeadLine(error = null,
        data = ArrayList(HeadLinesRepositoryImplTest.headLineItems())
    )
    private val error = ServerError(code= "Something went wrong", message = "Internal Server Error")
    private val failedServerHeadLine = ServerHeadLine(error = error, data = ArrayList(HeadLinesRepositoryImplTest.headLineItems())
    )
    private val sourceSuccessStubResult = Resource.Success(
        UIHeadLinesCollection(
            HeadLinesRepositoryImplTest.headLineItems()
        )
    )
    private val sourceErrorStubResult = Resource.Error<UIHeadLinesCollection>(
        "No Internet Connection"
    )

    private val exception = Exception("Something went wrong")

    @BeforeEach
    fun setUp(){
        MockitoAnnotations.openMocks(this)
        dataSource = HeadLineDataSourceImpl(database, networkService, connection, context)
    }

    @DisplayName("should return [Resource.Error] if networkConnection is false")
    @Test
    fun shouldReturnErrorWhenConnectionIsFalse() =  runTest {
        `when`(connection.isNetworkAvailable()).thenReturn(false)

        val actual = dataSource.remoteSource()

        verifyNoInteractions(networkService)
        assertEquals(sourceErrorStubResult.message, actual.message)
    }

    @DisplayName("should return [Resource.Success] if networkConnection is true and networkService is ok")
    @Test
    fun shouldReturnSuccessWhenConnectionIsTrueAndNetworkIsOK() =  runTest {
        `when`(connection.isNetworkAvailable()).thenReturn(true)
        `when`(networkService.getTopHeadLines(apiKey = BuildConfig.API_KEY)).thenAnswer{successfulServerHeadLine}
        `when`(database.headLineDao()).thenReturn(headLineDao)
        `when`(headLineDao.getTopHeadLines()).thenAnswer{HeadLinesRepositoryImplTest.headLineItems()}

        val actual = dataSource.remoteSource()

        verify(database, times(2)).headLineDao()
        verify(headLineDao, times(1)).getTopHeadLines()
        verify(networkService, times(1)).getTopHeadLines(apiKey = BuildConfig.API_KEY)
        assertEquals(sourceSuccessStubResult.data, actual.data)
    }

    @DisplayName("should return [Resource.Error] if networkConnection is true and networkService is not ok")
    @Test
    fun shouldReturnErrorWhenConnectionIsTrueAndNetworkIsNotOK() =  runTest {
        val expected = "Internal Server Error"
        `when`(connection.isNetworkAvailable()).thenReturn(true)
        `when`(networkService.getTopHeadLines(apiKey = BuildConfig.API_KEY)).thenAnswer{failedServerHeadLine}
        `when`(database.headLineDao()).thenReturn(headLineDao)
        `when`(headLineDao.getTopHeadLines()).thenAnswer{HeadLinesRepositoryImplTest.headLineItems()}

        val actual = dataSource.remoteSource()

        verifyNoInteractions(database)
        verify(networkService, times(1)).getTopHeadLines(apiKey = BuildConfig.API_KEY)
        assertEquals(expected, actual.message)
    }

    @DisplayName("should return [Resource.Success] if localSource is called")
    @Test
    fun shouldReturnSuccessWhenLocalSourceIsCalled() =  runTest {
        `when`(database.headLineDao()).thenReturn(headLineDao)
        `when`(headLineDao.getTopHeadLines()).thenAnswer{HeadLinesRepositoryImplTest.headLineItems()}

        val actual = dataSource.localSource()

        verify(database, times(1)).headLineDao()
        verify(headLineDao, times(1)).getTopHeadLines()
        assertEquals(sourceSuccessStubResult.data, actual.data)
    }
}