package com.deeosoft.pasteltest.infrastructure.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo

class Connection(context: Context) {
    private val cm: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = cm.activeNetwork
    private val networkCapabilities = cm.getNetworkCapabilities(network)

    fun isNetworkAvailable(): Boolean {
        return when{
            networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> true
            networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> true
            networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) == true -> true
            networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) == true -> true
            else -> false
        }
    }
    fun getDownStreamBPS(): Int? {
        return networkCapabilities?.linkDownstreamBandwidthKbps
    }
    fun getUpStreamBPS(): Int?{
        return networkCapabilities?.linkUpstreamBandwidthKbps
    }
}