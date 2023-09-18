package com.deeosoft.pasteltest.infrastructure.util

interface Connection {
    fun isNetworkAvailable(): Boolean
    fun getDownStreamBPS(): Int?
    fun getUpStreamBPS(): Int?
}