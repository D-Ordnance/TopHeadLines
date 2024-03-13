package com.deeosoft.deonews.infrastructure.util

interface Connection {
    fun isNetworkAvailable(): Boolean
    fun getDownStreamBPS(): Int?
    fun getUpStreamBPS(): Int?
}