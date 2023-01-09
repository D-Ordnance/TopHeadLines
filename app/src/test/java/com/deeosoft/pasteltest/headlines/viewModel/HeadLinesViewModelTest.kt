package com.deeosoft.pasteltest.headlines.viewModel

import com.deeosoft.pasteltest.headlines.repository.HeadLinesRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class HeadLinesViewModelTest {

    private val mockRepo: HeadLinesRepository = mock(HeadLinesRepository::class.java)

    private lateinit var viewModel: HeadLineViewModel

    @Before
    fun setUp() {
        viewModel = HeadLineViewModel(mockRepo)
    }

    @Test
    fun `determine_the_number_of_times_view_model_was_called`() = runBlocking{
        viewModel.getTopHeadLine(true)

        verify (mockRepo, times(1)).getTopHeadLines(true)
    }
}