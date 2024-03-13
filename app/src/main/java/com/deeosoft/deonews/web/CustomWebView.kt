package com.deeosoft.deonews.web

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.deeosoft.deonews.R
import com.deeosoft.deonews.ui.theme.ToolbarBlack

class CustomWebView: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var url = ""
        var author = ""
        if(intent != null){
            url = intent.getStringExtra(WEB_URL).toString()
            author = intent.getStringExtra(AUTHOR).toString()
        }
        setContent {
            MainWebViewContent(url, author)
        }
    }

    companion object{
        private const val WEB_URL = "web_url"
        private const val AUTHOR = "author"
        fun startActivity(context: Context,url: String?, author: String?){
            context.startActivity(
                Intent(context, CustomWebView::class.java)
                .putExtra(WEB_URL, url)
                .putExtra(AUTHOR, author ?: "Anonymous"))
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainWebViewContent(url: String, author: String) {
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = ToolbarBlack,
                title = {
                    Text(author, color = Color.White,
                    ) }) },
        content = { MyContent(url) }
    )
}


@Composable
fun MyContent(url: String){
    val isWebViewVisible = remember{mutableStateOf(true)}
    val webErrorMessage = remember {mutableStateOf("")}
    if(!isWebViewVisible.value){
        customWebViewErrorLayout(webErrorMessage.value)
    }else{
        AndroidView(factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = object : WebViewClient(){
                    override fun onReceivedError(
                        view: WebView?,
                        request: WebResourceRequest?,
                        error: WebResourceError?
                    ) {
                        isWebViewVisible.value = false
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            webErrorMessage.value = error?.description.toString()
                        }else{
                            webErrorMessage.value = "An error occurred, Check your internet connection and try again"
                        }
                    }
                }
                loadUrl(url)
            }
        }, update = {
            it.loadUrl(url)
        })
    }

}

@Composable
fun customWebViewErrorLayout(errorMessage: String){
    Box(modifier = Modifier.fillMaxSize()){
        Column(modifier = Modifier
            .fillMaxSize()
            .align(Alignment.Center),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painter = painterResource(R.drawable.network_error),
                contentDescription = "Network Error"
            )
            Text(text = "An error occurred", fontSize = 16.sp)
            Text(text = "ERROR: $errorMessage\n")
        }

    }
}