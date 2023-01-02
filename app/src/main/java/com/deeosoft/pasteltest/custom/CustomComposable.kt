package com.deeosoft.pasteltest.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.deeosoft.pasteltest.db.model.HeadLineItem

@Composable
fun ItemCard(
    modifier: Modifier,
    item: HeadLineItem,
    placeholder: Painter
){
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        AsyncImage(modifier = Modifier.fillMaxSize(),
            model = ImageRequest.Builder(LocalContext.current)
                .data(item.urlToImage)
                .crossfade(true)
                .build(),
            placeholder = placeholder,
            contentDescription = item.author,
            contentScale = ContentScale.Crop)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.Black.copy(alpha = 0.6f)
                )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            Column(modifier = Modifier.align
                (Alignment.BottomStart)
            ) {
                Text(
                    text = item.title,
                    fontSize = 16.sp,
                    color = Color.White,
                )
                Text(
                    text = item.author ?: "Anonymous",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.7f),
                )
            }

        }
    }
}
