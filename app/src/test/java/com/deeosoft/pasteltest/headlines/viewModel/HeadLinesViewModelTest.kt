package com.deeosoft.pasteltest.headlines.viewModel

import com.deeosoft.pasteltest.headlines.repository.HeadLinesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HeadLinesViewModelTest {

    @Mock
    private lateinit var mockRepo: HeadLinesRepository

    private lateinit var viewModel: HeadLineViewModel

    @Before
    fun setUp(){
        MockitoAnnotations.openMocks(this)
        viewModel = HeadLineViewModel(mockRepo)

    }

    /**
     * To make this test pass use
     * comment out the the loading livedata
     * inside the {if(forceServer)} in the [HeadLineViewModel]
     * viewModel class, the code itself breaks but the test passes
     * Increase the times to 2 and it failed.
     * Check how to perfectly mock a viewModel and a repository
     * so as to unit test them that doing an instrument testing
     * */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `determine_the_number_of_times_view_model_was_called`() = runTest{
        viewModel.getTopHeadLine(true)

        launch{
            verify (mockRepo, times(1)).getTopHeadLines(true)
        }
    }
}