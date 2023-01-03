package com.deeosoft.pasteltest.headlines.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.deeosoft.pasteltest.headlines.db.model.HeadLineItem
import com.deeosoft.pasteltest.headlines.db.model.HeadLineItemWithoutAutoGeneratingTestCase

@Database(
    entities = [HeadLineItem::class, HeadLineItemWithoutAutoGeneratingTestCase::class],
    version = 1,
)
abstract class HeadLineDatabase : RoomDatabase() {
    abstract fun headLineDao(): HeadLineDao
    abstract fun headLineItemWithoutAutoGeneratingTestDao(): HeadLineItemWithoutAutoGeneratingTestDao
}