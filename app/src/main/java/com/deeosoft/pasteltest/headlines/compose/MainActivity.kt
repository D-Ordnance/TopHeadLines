package com.deeosoft.pasteltest.headlines.compose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.deeosoft.pasteltest.R
import com.deeosoft.pasteltest.headlines.custom.ItemCard
import com.deeosoft.pasteltest.headlines.db.model.HeadLineItem
import com.deeosoft.pasteltest.ui.theme.PastelTestTheme
import com.deeosoft.pasteltest.ui.theme.TitleBlack
import com.deeosoft.pasteltest.util.format
import com.deeosoft.pasteltest.headlines.viewModel.HeadLineViewModel
import com.deeosoft.pasteltest.web.CustomWebView
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PastelTestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = TitleBlack
                ) {
                    MainScreen()
                }
            }
        }
    }
}

var calledOnce = false

@Composable
fun MainScreen(){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        ToolbarAndHeaderComposable()
        SwipeRefreshList()
    }
}

@Composable
fun ToolbarAndHeaderComposable(){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.news_feed),
                    color = Color.White
                )
            },
        )
        Text(text = stringResource(R.string.top_news),
            fontSize = 15.sp,
            color = Color.White
        )
    }
}

@Composable
fun ContentScreen(viewModel: HeadLineViewModel,
                  selectedItem: (HeadLineItem) -> Unit){
    val context = LocalContext.current
    val loading = viewModel.loading.observeAsState()
    val failure = viewModel.failure.observeAsState()
    val success = viewModel.success.observeAsState()
    if(loading.value == true){
        println("loading ...")
    }
    if(failure.value != null){
        Toast.makeText(
            context,
            viewModel.failure.value?.format(context) ?: context.getString(R.string.default_error_message),
            Toast.LENGTH_LONG)
            .show()
    }
    if(success.value != null){
        LazyColumn{
            items(viewModel.success.value!!.articles) {
                if (it != null) {
                    ItemCard(item = it,
                        placeholder = painterResource(id = R.drawable.image_placeholder),
                        modifier = Modifier
                            .height(200.dp)
                            .clickable {
                                selectedItem(it)
                            })
                }
            }
        }
    }
}

@Composable
fun SwipeRefreshList(viewModel: HeadLineViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val headLineViewModel = remember{viewModel}
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = headLineViewModel.loading.value!!)
    if(!calledOnce) {
        viewModel.getTopHeadLine()
        calledOnce = true
    }

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = { viewModel.getTopHeadLine(true) }
    ) {
        ContentScreen(headLineViewModel, selectedItem = {
            CustomWebView.startActivity(context, it.url, it.author)
        })
    }
}
