package com.deeosoft.pasteltest.headlines.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.deeosoft.pasteltest.R
import com.deeosoft.pasteltest.headlines.compose.ToolbarAndHeaderComposable
import org.junit.Rule
import org.junit.Test

class MainActivityTest{
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val titleTextNode = hasText("Top News") and !hasClickAction()

    @Test
    fun `determine_if_title_text_is_exist`(){
        val headerText = rule.activity.getString(R.string.top_news)
        rule.setContent {
            ToolbarAndHeaderComposable()
        }

        rule.onNode(titleTextNode).performClick()
        rule.onNodeWithText(headerText).assertExists()
    }

    @Test
    fun `determine_if_toolbar_text_is_exist`(){
        val toolBarText = rule.activity.getString(R.string.news_feed)
        rule.setContent {
            ToolbarAndHeaderComposable()
        }

        rule.onNodeWithText(toolBarText).assertExists()
    }
}