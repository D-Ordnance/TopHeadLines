package com.deeosoft.pasteltest.headlines.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.deeosoft.pasteltest.headlines.db.HeadLineDatabase
import com.deeosoft.pasteltest.headlines.db.model.HeadLineItem
import com.deeosoft.pasteltest.infrastructure.network.NetworkService
import javax.inject.Inject

//@OptIn(ExperimentalPagingApi::class)
//class HeadLineMediator @Inject constructor(
//    private val networkService: NetworkService,
//    private val dataService: HeadLineDatabase
//): RemoteMediator<Int, HeadLineItem>() {
//    override suspend fun load(
//        loadType: LoadType,
//        state: PagingState<Int, HeadLineItem>
//    ): MediatorResult {
//
//        return try {
//            val loadKey = when (loadType) {
//                LoadType.REFRESH -> {}
//                LoadType.PREPEND -> {}
//                LoadType.APPEND -> {}
//            }
//        }catch (ex: Exception){
//            ex.printStackTrace()
//        }
//    }


//}