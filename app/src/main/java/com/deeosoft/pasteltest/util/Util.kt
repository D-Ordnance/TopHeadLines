package com.deeosoft.pasteltest.util

import android.content.Context
import com.deeosoft.pasteltest.R

fun String.format(context: Context): String{
    var response = context.getString(R.string.default_error_message)
    if(this.contains("No address associated with hostname"))
        response = context.getString(R.string.ui_network_error_message)
    return response
}