package com.deeosoft.pasteltest.headlines.data.repository

import com.deeosoft.pasteltest.headlines.data.datasource.HeadLineDataSource
import com.deeosoft.pasteltest.headlines.data.model.HeadLineItem
import com.deeosoft.pasteltest.headlines.data.model.UIHeadLinesCollection
import com.deeosoft.pasteltest.headlines.domain.repository.HeadLineRepository
import com.deeosoft.pasteltest.headlines.domain.repository.Resource
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

typealias body<T> = (result: MutableList<T>) -> Unit

@OptIn(ExperimentalCoroutinesApi::class)
class HeadLinesRepositoryImplTest {
    @Mock
    private lateinit var dataSource: HeadLineDataSource
    private lateinit var repository: HeadLineRepository

    private val forceServer = true
    private val expectedResult = headLineItems()
    private val sourceSuccessStubResult = Resource.Success(
        UIHeadLinesCollection(
            headLineItems()
        )
    )
    private val sourceErrorStubResult = Resource.Error<UIHeadLinesCollection>(expectedErrorMsg)

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = HeadLineRepositoryImpl(dataSource)
    }

    private fun localSourceArrangeAndAct(
        expected: Resource<UIHeadLinesCollection>,
        body: body<Resource<UIHeadLinesCollection>>
    ) = runTest {
        `when`(dataSource.localSource()).thenReturn(expected)
        val result = mutableListOf<Resource<UIHeadLinesCollection>>()
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            repository.getTopHeadLines(!forceServer).toList(result)
        }
        body(result)
    }

    @Test
    fun `should verify localSource interaction and return Success when !forceServe is called`() =
        runTest {
            localSourceArrangeAndAct(
                sourceSuccessStubResult
            ) {
                assertEquals(expectedResult, it[0].data?.articles!!)
                launch {
                    verify(dataSource, times(1)).localSource()
                    verify(dataSource, times(1)).remoteSource()
                }
            }
        }

    @Test
    fun `should verify localSource interaction and return Error when !forceServe is called`() =
        runTest {
            localSourceArrangeAndAct(
                sourceErrorStubResult
            ) {
                assertEquals(expectedErrorMsg, it[0].message!!)
                launch {
                    verify(dataSource, times(1)).localSource()
                    verify(dataSource, times(1)).remoteSource()
                }
            }
        }

    private fun remoteSourceArrangeAndAct(
        expected: Resource<UIHeadLinesCollection>,
        body: body<Resource<UIHeadLinesCollection>>
    ) = runTest {
        `when`(dataSource.remoteSource()).thenReturn(expected)
        val result = mutableListOf<Resource<UIHeadLinesCollection>>()
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            repository.getTopHeadLines(forceServer).toList(result)
        }
        body(result)
    }
    @Test
    fun `should verify remoteSource interaction and return Success when forceServe is called`() =
        runTest {
            remoteSourceArrangeAndAct(sourceSuccessStubResult){
                assertEquals(expectedResult, it[0].data?.articles!!)
                launch{
                    verify(dataSource, times(0)).localSource()
                    verify(dataSource, times(1)).remoteSource()
                }
            }
        }

    @Test
    fun `should verify remoteSource interaction and return Error when forceServe is called`() =
        runTest {
            remoteSourceArrangeAndAct(sourceErrorStubResult){
                assertEquals(expectedErrorMsg, it[0].message!!)
                launch{
                    verify(dataSource, times(0)).localSource()
                    verify(dataSource, times(1)).remoteSource()
                }
            }
        }

    private fun localAndRemoteSourceArrangeAndAct(
        expected: Resource<UIHeadLinesCollection>,
        body: body<Resource<UIHeadLinesCollection>>
    ) = runTest {
        `when`(dataSource.localSource()).thenReturn(expected)
        `when`(dataSource.remoteSource()).thenReturn(expected)
        val result = mutableListOf<Resource<UIHeadLinesCollection>>()
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            repository.getTopHeadLines(!forceServer).toList(result)
        }
        body(result)
    }
    @Test
    fun `should verify localSource and remoteSource interaction and return Error when !forceServe is called`() =
        runTest {
            localAndRemoteSourceArrangeAndAct(sourceErrorStubResult){
                assertEquals(2, it.size)
                launch {
                    verify(dataSource, times(1)).localSource()
                    verify(dataSource, times(1)).remoteSource()
                }
            }
        }

    @Test
    fun `should verify localSource and remoteSource interaction and return Success when !forceServe is called`() =
        runTest {
            localAndRemoteSourceArrangeAndAct(sourceSuccessStubResult){
                assertEquals(2, it.size)
                launch {
                    verify(dataSource, times(1)).localSource()
                    verify(dataSource, times(1)).remoteSource()
                }
            }
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