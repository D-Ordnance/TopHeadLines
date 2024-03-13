package com.deeosoft.deonews.headlines.db

import androidx.room.*
import com.deeosoft.deonews.headlines.data.model.HeadLineItem
import com.deeosoft.deonews.headlines.data.model.HeadLineItemWithoutAutoGeneratingTestCase

@Dao
interface HeadLineDao {

    @Query("SELECT * from HeadLineItem ORDER BY id DESC")
    suspend fun getTopHeadLines(): List<HeadLineItem?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: HeadLineItem)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: List<HeadLineItem?>)

//    @Query("SELECT * from HeadLineItem")
//    suspend fun getPagingSource(): PagingSource<Int, HeadLineItem>
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