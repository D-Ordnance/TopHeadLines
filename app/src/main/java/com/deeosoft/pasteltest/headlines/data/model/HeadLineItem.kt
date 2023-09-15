package com.deeosoft.pasteltest.headlines.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "HeadLineItem",
    indices = [
        Index(
            value = ["title", "url", "urlToImage"],
            unique = true)]
)
data class HeadLineItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val author: String?,
    val url: String?,
    val urlToImage: String?
)

@Entity(tableName = "HeadLineItemWithoutPrimaryKey")
data class HeadLineItemWithoutAutoGeneratingTestCase(
    @PrimaryKey val id: Int = 0,
    val title: String,
    val author: String?,
    val url: String?,
    val urlToImage: String?
)

data class ServerHeadLine(
    val status: String,
    val totalResults: Int,
    val articles: ArrayList<HeadLineItem>
)

data class UIHeadLinesCollection(
    val articles: List<HeadLineItem?>
)
