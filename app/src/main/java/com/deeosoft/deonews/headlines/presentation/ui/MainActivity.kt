package com.deeosoft.deonews.headlines.presentation.ui

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.deeosoft.deonews.R
import com.deeosoft.deonews.headlines.presentation.custom.ItemCard
import com.deeosoft.deonews.headlines.data.model.HeadLineItem
import com.deeosoft.deonews.util.format
import com.deeosoft.deonews.headlines.presentation.viewModel.HeadLineViewModel
import com.deeosoft.deonews.ui.theme.PastelTestTheme
import com.deeosoft.deonews.web.CustomWebView
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val headLineViewModel by viewModels<HeadLineViewModel>()
        headLineViewModel.getTopHeadLine()
        setContent {
            PastelTestTheme{
                GetScaffold(headLineViewModel)
            }
        }
    }
}

private fun showStateMessage(context: Context, msg: String){
    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
}

@Composable
fun GetScaffold(viewModel: HeadLineViewModel = hiltViewModel()){
    Scaffold(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colors.primary),
        topBar = {
            TopBarLayout()
        },
        content = {
            ContentMainScreen(it, viewModel)
        }
    )
}

@Composable
fun TopBarLayout(){
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.news_feed),
                color = MaterialTheme.colors.secondary
            )
        },
        backgroundColor = MaterialTheme.colors.primaryVariant
    )
}

@Composable
fun ContentMainScreen(padding: PaddingValues, viewModel: HeadLineViewModel){
    Column(
        Modifier
            .fillMaxSize()
            .padding(padding)
            .background(MaterialTheme.colors.primary)) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = stringResource(R.string.top_news),
            fontSize = 15.sp,
            color = MaterialTheme.colors.secondary,
        )
        SwipeRefreshListComposable(viewModel)
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
                    color = MaterialTheme.colors.secondary
                )
            },
        )
        Text(text = stringResource(R.string.top_news),
            fontSize = 15.sp,
            color = MaterialTheme.colors.secondary
        )
    }
}

@Composable
fun ContentScreen(viewModel: HeadLineViewModel,
                  selectedItem: (HeadLineItem) -> Unit){
    val context = LocalContext.current
    val failure = viewModel.failure.observeAsState()
    val success = viewModel.success.observeAsState()

    if(failure.value != null){
        showStateMessage(context, viewModel.failure.value?.format(context) ?: context.getString(R.string.default_error_message))
    }
    if(success.value != null){
        LazyColumn{
            items(viewModel.success.value!!.articles) {
                if (it != null) {
                    ItemCard(item = it,
                        placeholder = painterResource(id = R.drawable.image_placeholder),
                        fallback = painterResource(id = R.drawable.image_fall_back),
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
fun SwipeRefreshListComposable(viewModel: HeadLineViewModel = hiltViewModel()) {
    val context = LocalContext.current

    val isLoading by viewModel.loading.observeAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading!!)

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {
            (viewModel::getTopHeadLine)(true)
        }
    ) {
        ContentScreen(viewModel, selectedItem = {
            CustomWebView.startActivity(context, it.url, it.source)
        })
    }
}
