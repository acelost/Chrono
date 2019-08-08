package com.acelost.chronodemo.samples

import android.os.Bundle
import com.acelost.chrono.Timer
import com.acelost.chronodemo.SampleActivity

class MeasureLifecycleMethodsSample : SampleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val timer = Timer.startNew()
        super.onCreate(savedInstanceState)
        timer.stop()
    }

    override fun onStart() {
        val timer = Timer.startNew()
        super.onStart()
        timer.stop()
    }

    override fun onResume() {
        val timer = Timer.startNew()
        super.onResume()
        timer.stop()
    }

    override fun onPause() {
        val timer = Timer.startNew()
        super.onPause()
        timer.stop()
    }

    override fun onStop() {
        val timer = Timer.startNew()
        super.onStop()
        timer.stop()
    }

    override fun onDestroy() {
        val timer = Timer.startNew()
        super.onDestroy()
        timer.stop()
    }

}