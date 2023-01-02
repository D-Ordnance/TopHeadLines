package com.deeosoft.pasteltest.db

import androidx.room.*
import com.deeosoft.pasteltest.db.model.HeadLineItem
import com.deeosoft.pasteltest.db.model.HeadLineItemWithoutAutoGeneratingTestCase

@Dao
interface HeadLineDao {

    @Query("SELECT * from HeadLineItem")
    suspend fun getTopHeadLines(): List<HeadLineItem?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: HeadLineItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: List<HeadLineItem?>)
}

@Dao
interface HeadLineItemWithoutAutoGeneratingTestDao {

    @Query("SELECT * from HeadLineItemWithoutPrimaryKey")
    suspend fun getTopHeadLines(): List<HeadLineItemWithoutAutoGeneratingTestCase?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: HeadLineItemWithoutAutoGeneratingTestCase)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: List<HeadLineItemWithoutAutoGeneratingTestCase?>)
}