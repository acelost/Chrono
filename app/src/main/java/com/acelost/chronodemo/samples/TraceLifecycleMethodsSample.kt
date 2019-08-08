package com.acelost.chronodemo.samples

import android.os.Bundle
import com.acelost.chrono.Chrono
import com.acelost.chronodemo.SampleActivity

class TraceLifecycleMethodsSample : SampleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Chrono.start("lifecycle")
        super.onCreate(savedInstanceState)
        Chrono.capture("lifecycle", "created")
    }

    override fun onStart() {
        super.onStart()
        Chrono.capture("lifecycle", "started")
    }

    override fun onResume() {
        super.onResume()
        Chrono.capture("lifecycle", "resumed")
        Chrono.stop("lifecycle")
    }

}