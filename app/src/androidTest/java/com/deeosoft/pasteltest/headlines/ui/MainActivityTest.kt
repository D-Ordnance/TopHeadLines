package com.deeosoft.pasteltest.headlines.ui

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.deeosoft.pasteltest.BuildConfig
import com.deeosoft.pasteltest.R
import com.deeosoft.pasteltest.headlines.db.HeadLineDatabase
import com.deeosoft.pasteltest.headlines.repository.HeadLinesRepository
import com.deeosoft.pasteltest.headlines.viewModel.HeadLineViewModel
import com.deeosoft.pasteltest.infrastructure.network.NetworkService
import com.google.accompanist.swiperefresh.SwipeRefreshState
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit

class MainActivityTest{
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val titleTextNode = hasText("Top News") and !hasClickAction()

    private lateinit var viewModel: HeadLineViewModel
    @Before
    fun setUp(){
        val networkService = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .build().create(NetworkService::class.java)
        val database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            HeadLineDatabase::class.java
        ).allowMainThreadQueries().build()
        val headLinesRepository = HeadLinesRepository(networkService, database, rule.activity.application)
        viewModel = HeadLineViewModel(headLinesRepository)
    }

    @Test
    fun `determine_if_title_text_exist`(){
        val headerText = rule.activity.getString(R.string.top_news)
        rule.setContent {
            ToolbarAndHeaderComposable()
        }

        rule.onNode(titleTextNode).performClick()
        rule.onNodeWithText(headerText).assertExists()
    }

    @Test
    fun `determine_if_toolbar_text_exist`(){
        val toolBarText = rule.activity.getString(R.string.news_feed)
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