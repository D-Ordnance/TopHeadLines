package com.deeosoft.macrobenchmark

import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.uiautomator.By
import org.junit.Rule

class LazyColumnFrameTimingBenchmark {

    @get:
    Rule val macroBenchmarkRule = MacrobenchmarkRule()

    fun getScrollNestedFrameTimeBenchMark(){

    }

    private fun MacrobenchmarkScope.scrollNestedFrameTimeLazyColumn(){
//        val recycler = device.findObject(By.)
    }

}