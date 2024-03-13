package com.deeosoft.deonews.headlines.presentation.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.deeosoft.deonews.headlines.domain.repository.HeadLineRepository
import com.deeosoft.deonews.headlines.presentation.viewModel.HeadLineViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock

class MainActivityTest{
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Mock
    private lateinit var mockRepo: HeadLineRepository

    private lateinit var viewModel: HeadLineViewModel

    private val titleTextNode = hasText("Top News") and !hasClickAction()

    @Before
    fun setUp(){
        mockRepo = mock()
        viewModel = HeadLineViewModel(mockRepo)
    }

    @Test
    fun `determine_if_title_text_exist`(){
        val headerText = rule.activity.getString(com.deeosoft.deonews.R.string.top_news)
        rule.setContent {
            ToolbarAndHeaderComposable()
        }

        rule.onNode(titleTextNode).performClick()
        rule.onNodeWithText(headerText).assertExists()
    }

    @Test
    fun `determine_if_toolbar_text_exist`(){
        val toolBarText = rule.activity.getString(com.deeosoft.deonews.R.string.news_feed)
        rule.setContent {
            ToolbarAndHeaderComposable()
        }

        rule.onNodeWithText(toolBarText).assertExists()
    }

    @Test
    fun `determine_swipe_to_refresh_is_loading_state_change_on_swipe`(){

        rule.setContent { SwipeRefreshListComposable(viewModel) }

//        rule.onNode(hasStateDescription("isLoading")).printToLog("SwipeToRefreshExist")
    }
}