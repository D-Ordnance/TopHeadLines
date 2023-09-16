package com.deeosoft.pasteltest.headlines.presentation.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.deeosoft.pasteltest.headlines.data.model.UIHeadLinesCollection
import com.deeosoft.pasteltest.headlines.data.repository.HeadLinesRepositoryImplTest.Companion.expectedErrorMsg
import com.deeosoft.pasteltest.headlines.data.repository.HeadLinesRepositoryImplTest.Companion.headLineItems
import com.deeosoft.pasteltest.headlines.domain.repository.HeadLineRepository
import com.deeosoft.pasteltest.headlines.domain.repository.Resource
import com.deeosoft.pasteltest.util.MainDispatcherRule
import com.deeosoft.pasteltest.util.getAwaitValueTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class HeadLinesViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val dispatcher = StandardTestDispatcher(TestCoroutineScheduler())
    @Mock
    private lateinit var mockRepo: HeadLineRepository
    private lateinit var viewModel: HeadLineViewModel
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = HeadLineViewModel(mockRepo, dispatcher)
    }
    private fun verifyMockRepoInteraction(forceServer: Boolean) = runTest {
        viewModel.getTopHeadLine(forceServer)
        dispatcher.scheduler.advanceUntilIdle()
        launch{
            verify(mockRepo, times(1)).getTopHeadLines(forceServer)
        }
    }
    @Test
    fun should_verify_headline_repository_was_called_once_when_force_server_is_true() {
        verifyMockRepoInteraction(true)
    }
    @Test
    fun should_verify_headline_repository_was_called_once_when_force_server_is_false() {
        verifyMockRepoInteraction(false)
    }
    @Test
    fun should_assert_it_returns_Error_and_message_is_errorMessage_failed_when_error_is_returned(){
        mockRepoArrangeAndAct(Resource.Error(expectedErrorMsg)) {
            val actual = viewModel.failure.getAwaitValueTest()

            assertEquals(expectedErrorMsg, actual)
        }
    }
    @Test
    fun should_assert_it_emits_Success_and_NEW_DAY_RESOLUTION_is_part_of_the_article_title_returned(){
        val expectedTitle = "NEW DAY RESOLUTION"
        mockRepoArrangeAndAct(Resource.Success(UIHeadLinesCollection(headLineItems()))){
            val headLineCollection = viewModel.success.getAwaitValueTest()
            var actualTitle = ""
            for(actual in headLineCollection.articles){
                if(actual?.title.equals("NEW DAY RESOLUTION"))actualTitle = actual?.title.toString()
            }
            assertEquals(expectedTitle, actualTitle)
        }
    }
    private fun mockRepoArrangeAndAct(value: Resource<UIHeadLinesCollection>, body: () -> Unit)= runTest {
        `when`(mockRepo.getTopHeadLines(anyBoolean())).thenReturn(
            flow { emit(value) }
        )
        viewModel.getTopHeadLine(true)
        dispatcher.scheduler.advanceUntilIdle()
        body()
    }
}