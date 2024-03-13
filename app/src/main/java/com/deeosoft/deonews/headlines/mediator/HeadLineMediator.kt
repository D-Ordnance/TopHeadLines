package com.deeosoft.deonews.headlines.mediator

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