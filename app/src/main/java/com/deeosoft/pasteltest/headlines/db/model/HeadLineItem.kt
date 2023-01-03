package com.deeosoft.pasteltest.headlines.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "HeadLineItem")
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
