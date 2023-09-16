package com.deeosoft.pasteltest.headlines.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.deeosoft.pasteltest.headlines.data.datasource.HeadLineDataSource
import com.deeosoft.pasteltest.headlines.data.model.HeadLineItem
import com.deeosoft.pasteltest.headlines.data.model.UIHeadLinesCollection
import com.deeosoft.pasteltest.headlines.domain.repository.HeadLineRepository
import com.deeosoft.pasteltest.headlines.domain.repository.Resource
import com.deeosoft.pasteltest.util.MainDispatcherRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions

@OptIn(ExperimentalCoroutinesApi::class)
class HeadLinesRepositoryImplTest {
    @Mock
    private lateinit var dataSource: HeadLineDataSource
    private lateinit var repository: HeadLineRepository

    private val forceServer = true
    private val expectedResult = headLineItems()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = HeadLineRepositoryImpl(dataSource)
    }

    @Test
    fun `should verify localSource interaction and return Success when !forceServe is called`() =
        runTest {
            `when`(dataSource.localSource()).thenReturn(
                Resource.Success(
                    UIHeadLinesCollection(
                        headLineItems()
                    )
                )
            )
            val result = mutableListOf<Resource<UIHeadLinesCollection>>()
            backgroundScope.launch(UnconfinedTestDispatcher()) {
                repository.getTopHeadLines(!forceServer).toList(result)
            }

            assertEquals(expectedResult, result[0].data?.articles!!)
            verify(dataSource, times(1)).localSource()
            verify(dataSource, times(1)).remoteSource()
        }

    @Test
    fun `should verify localSource interaction and return Error when !forceServe is called`() =
        runTest {
            `when`(dataSource.localSource()).thenReturn(
                Resource.Error(expectedErrorMsg)
            )
            val result = mutableListOf<Resource<UIHeadLinesCollection>>()
            backgroundScope.launch(UnconfinedTestDispatcher()) {
                repository.getTopHeadLines(!forceServer).toList(result)
            }

            assertEquals(expectedErrorMsg, result[0].message!!)
            verify(dataSource, times(1)).localSource()
            verify(dataSource, times(1)).remoteSource()
        }

    @Test
    fun `should verify remoteSource interaction and return Success when forceServe is called`() =
        runTest {
            `when`(dataSource.remoteSource()).thenReturn(
                Resource.Success(UIHeadLinesCollection(expectedResult))
            )
            val result = mutableListOf<Resource<UIHeadLinesCollection>>()
            backgroundScope.launch(UnconfinedTestDispatcher()) {
                repository.getTopHeadLines(forceServer).toList(result)
            }

            assertEquals(expectedResult, result[0].data?.articles!!)
            verify(dataSource, times(0)).localSource()
            verify(dataSource, times(1)).remoteSource()
        }

    @Test
    fun `should verify remoteSource interaction and return Error when forceServe is called`() =
        runTest {
            `when`(dataSource.remoteSource()).thenReturn(
                Resource.Error(expectedErrorMsg)
            )
            val result = mutableListOf<Resource<UIHeadLinesCollection>>()
            backgroundScope.launch(UnconfinedTestDispatcher()) {
                repository.getTopHeadLines(forceServer).toList(result)
            }

            assertEquals(expectedErrorMsg, result[0].message!!)
            verify(dataSource, times(0)).localSource()
            verify(dataSource, times(1)).remoteSource()
        }

    @Test
    fun `should verify localSource and remoteSource interaction and return Error when !forceServe is called`() =
        runTest {
            `when`(dataSource.localSource()).thenReturn(
                Resource.Error(expectedErrorMsg)
            )
            `when`(dataSource.remoteSource()).thenReturn(
                Resource.Error(expectedErrorMsg)
            )
            val result = mutableListOf<Resource<UIHeadLinesCollection>>()
            backgroundScope.launch(UnconfinedTestDispatcher()) {
                repository.getTopHeadLines(!forceServer).toList(result)
            }

            assertEquals(2, result.size)
            verify(dataSource, times(1)).localSource()
            verify(dataSource, times(1)).remoteSource()
        }

    @Test
    fun `should verify localSource and remoteSource interaction and return Success when !forceServe is called`() =
        runTest {
            `when`(dataSource.localSource()).thenReturn(
                Resource.Success(UIHeadLinesCollection(expectedResult))
            )
            `when`(dataSource.remoteSource()).thenReturn(
                Resource.Success(UIHeadLinesCollection(expectedResult))
            )
            val result = mutableListOf<Resource<UIHeadLinesCollection>>()
            backgroundScope.launch(UnconfinedTestDispatcher()) {
                repository.getTopHeadLines(!forceServer).toList(result)
            }

            assertEquals(2, result.size)
            verify(dataSource, times(1)).localSource()
            verify(dataSource, times(1)).remoteSource()
        }

    companion object {
        fun headLineItems(): List<HeadLineItem?> {
            return arrayListOf(
                HeadLineItem(
                    id = 0,
                    title = "NEW YEAR RESOLUTION",
                    author = "Dolapo Olakanmi",
                    url = "https://developer.android.com",
                    urlToImage = "https://ichef.bbci.co.uk/news/1024/branded_news/F949/production/_128171836_gettyimages-1453494652.jpg"
                ),
                HeadLineItem(
                    id = 1,
                    title = "NEW MONTH RESOLUTION",
                    author = "Deeosoft",
                    url = "https://developer.android.com/",
                    urlToImage = "https://ichef.bbci.co.uk/news/1024/branded_news/F949/production/_128171836_gettyimages-1453494652.jpg"
                ),
                HeadLineItem(
                    id = 2,
                    title = "NEW DAY RESOLUTION",
                    author = "Dolapo",
                    url = "https://www.developer.android.com/",
                    urlToImage = "https://ichef.bbci.co.uk/news/1024/branded_news/F949/production/_128171836_gettyimages-1453494652.jpg"
                )
            )
        }
        const val expectedErrorMsg = "Failed to load"
    }
}