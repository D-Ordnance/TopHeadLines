package com.deeosoft.pasteltest.viewModel

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.deeosoft.pasteltest.BuildConfig
import com.deeosoft.pasteltest.db.HeadLineDatabase
import com.deeosoft.pasteltest.network.NetworkService
import com.deeosoft.pasteltest.repository.HeadLinesRepository
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit

@RunWith(AndroidJUnit4::class)
class HeadLineViewModelTest: TestCase(){

    private lateinit var viewModel: HeadLineViewModel
    private lateinit var database: HeadLineDatabase

    public override fun setUp() {
        super.setUp()
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, HeadLineDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .build()
        val networkService = retrofit.create(NetworkService::class.java)
        val repository = HeadLinesRepository(networkService, database)

        viewModel = HeadLineViewModel(repository)
    }

    @Test
    fun `determine_local_db_is_not_null_after_first_insertion`(){

//        database.headLineDao().insert()
        viewModel.getTopHeadLine(false)
    }
}