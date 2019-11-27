package com.acelost.chronodemo.samples

import android.os.Bundle
import com.acelost.chrono.Chrono
import com.acelost.chrono.Timer
import com.acelost.chronodemo.SampleActivity
import java.util.*

class HeavyMethodSample : SampleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //heavyMethod()
        repeat(10) {
            hm()
        }
        Chrono.bunchAverage("heavy-method-bunch")
        Chrono.bunchTotal("heavy-method-bunch")
    }

    private fun heavyMethod() {
        val timer = Timer.startNew("heavy-method")
        Thread.sleep(1000)
        timer.capture("after a long sleep")
        Thread.sleep(300)
        timer.capture("after a quick sleep")
        Thread.sleep(500)
        timer.stop()
    }

    private fun hm() {
        val timer = Timer.startNew("heavy-method").toBunch("heavy-method-bunch")
        Thread.sleep(Random().nextInt(500).toLong())
        timer.stop()
    }

}