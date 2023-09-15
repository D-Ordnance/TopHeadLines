package com.deeosoft.pasteltest.util

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Gets the value of a [LiveData] or waits for it to have one, with a timeout.
 *
 * Use this extension from host-side (JVM) tests. It's recommended to use it alongside
 * `InstantTaskExecutorRule` or a similar mechanism to execute tasks synchronously.
 */

@VisibleForTesting(otherwise = VisibleForTesting.NONE)
fun <T> LiveData<T>.getAwaitValueTest(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {}
): T {

    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object: Observer<T>{
        override fun onChanged(t: T) {
            data = t
            latch.countDown()
            this@getAwaitValueTest.removeObserver(this)
        }
    }
    this.observeForever(observer)
    try {
        afterObserve.invoke()

        // Don't wait indefinitely if the LiveData is not set.
        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }

    } finally {
        this.removeObserver(observer)
    }

    @Suppress("UNCHECKED_CAST")
    return data as T

}