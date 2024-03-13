package com.deeosoft.deonews.headlines.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "HeadLineItem",
    indices = [
        Index(
            value = ["title", "url", "image_url"],
            unique = true)]
)
data class HeadLineItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val source: String?,
    val url: String?,
    val image_url: String?
)

@Entity(tableName = "HeadLineItemWithoutPrimaryKey")
data class HeadLineItemWithoutAutoGeneratingTestCase(
    @PrimaryKey val id: Int = 0,
    val title: String,
    val source: String?,
    val url: String?,
    val image_url: String?
)

data class ServerHeadLine(
//    val status: String,
//    val totalResults: Int,
    val error: ServerError?,
    val data: ArrayList<HeadLineItem>
)

data class UIHeadLinesCollection(
    val articles: List<HeadLineItem?>
)

data class ServerError(
    val code: String,
    val message: String
)
