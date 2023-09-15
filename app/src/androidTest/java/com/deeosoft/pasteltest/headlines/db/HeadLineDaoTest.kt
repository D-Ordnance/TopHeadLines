package com.deeosoft.pasteltest.headlines.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.deeosoft.pasteltest.headlines.data.model.HeadLineItemWithoutAutoGeneratingTestCase
import com.deeosoft.pasteltest.headlines.db.HeadLineDao
import com.deeosoft.pasteltest.headlines.db.HeadLineDatabase
import com.deeosoft.pasteltest.headlines.db.HeadLineItemWithoutAutoGeneratingTestDao
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.stream.Stream.concat

@RunWith(AndroidJUnit4::class)
class HeadLineDaoTest: TestCase() {

    private lateinit var database: HeadLineDatabase
    lateinit var databaseDao: HeadLineDao
    private lateinit var databaseDao2: HeadLineItemWithoutAutoGeneratingTestDao

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, HeadLineDatabase::class.java)
            .allowMainThreadQueries()
            .build()
//        databaseDao = database.headLineDao()
        databaseDao2 = database.headLineItemWithoutAutoGeneratingTestDao()
    }

    @After
    fun teardown(){
        database.close()
    }

    @Test
    fun `insert_single_headLine_Item_to_database_and_check_if_it_exist`() = runBlocking{

        val headLineItem = HeadLineItemWithoutAutoGeneratingTestCase(
            id = 0,
            title = "NEW YEAR RESOLUTION",
            author = "Dolapo Olakanmi",
            url = "https://developer.android.com",
            urlToImage = "https://ichef.bbci.co.uk/news/1024/branded_news/F949/production/_128171836_gettyimages-1453494652.jpg"
        )

//        databaseDao.insert(headLineItem)
        databaseDao2.insert(headLineItem)
        val fetchedResult = databaseDao2.getTopHeadLines()
        val listWithOutId: MutableList<HeadLineItemWithoutAutoGeneratingTestCase> = mutableListOf()
        fetchedResult.forEach {
            listWithOutId.add(it!!)
        }
        assertTrue(listWithOutId.contains(headLineItem))
    }

    @Test
    fun `insert_list_of_headLine_Item_to_database_and_check_if_it_exist_using_size`() = runBlocking{
        //we cannot have similar item because there is a conflict strategy
        val headLineItem = HeadLineItemWithoutAutoGeneratingTestCase(
            id = 0,
            title = "NEW YEAR RESOLUTION",
            author = "Dolapo Olakanmi",
            url = "https://developer.android.com",
            urlToImage = "https://ichef.bbci.co.uk/news/1024/branded_news/F949/production/_128171836_gettyimages-1453494652.jpg"
        )
        val headLineItem2 = HeadLineItemWithoutAutoGeneratingTestCase(
            id = 1,
            title = "NEW MONTH RESOLUTION",
            author = "Deeosoft",
            url = "https://developer.android.com/",
            urlToImage = "https://ichef.bbci.co.uk/news/1024/branded_news/F949/production/_128171836_gettyimages-1453494652.jpg"
        )
        val headLineItem3 = HeadLineItemWithoutAutoGeneratingTestCase(
            id = 2,
            title = "NEW DAY RESOLUTION",
            author = "Dolapo",
            url = "https://www.developer.android.com/",
            urlToImage = "https://ichef.bbci.co.uk/news/1024/branded_news/F949/production/_128171836_gettyimages-1453494652.jpg"
        )

        val sizeOfHeadLineEntityBeforeNewInsertion = databaseDao2.getTopHeadLines().size
        val newHeadLinesEntity = mutableListOf<HeadLineItemWithoutAutoGeneratingTestCase>()
        newHeadLinesEntity.add(headLineItem)
        newHeadLinesEntity.add(headLineItem2)
        newHeadLinesEntity.add(headLineItem3)

        val sizeOfNewHeadLinesEntity =  newHeadLinesEntity.size

        databaseDao2.insert(newHeadLinesEntity)
        val sizeOfHeadLineEntityAfterNewInsertion = databaseDao2.getTopHeadLines().size
        assertEquals(sizeOfHeadLineEntityBeforeNewInsertion + sizeOfNewHeadLinesEntity, sizeOfHeadLineEntityAfterNewInsertion)
    }
}