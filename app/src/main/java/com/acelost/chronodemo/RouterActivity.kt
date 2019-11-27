package com.acelost.chronodemo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.acelost.chronodemo.samples.HeavyMethodSample
import com.acelost.chronodemo.samples.MeasureLifecycleMethodsSample
import com.acelost.chronodemo.samples.TraceFragmentTransactionSample
import com.acelost.chronodemo.samples.TraceLifecycleMethodsSample

class RouterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_router)
        bind<MeasureLifecycleMethodsSample>(R.id.measure_lifecycle_methods, R.string.measure_lifecycle_methods)
        bind<TraceLifecycleMethodsSample>(R.id.trace_lifecycle_methods, R.string.trace_lifecycle_methods)
        bind<TraceFragmentTransactionSample>(R.id.trace_fragment_transaction, R.string.trace_fragment_transaction)
        bind<HeavyMethodSample>(R.id.trace_heavy_method, R.string.trace_heavy_method)
    }

    private inline fun <reified A : Activity> bind(@IdRes buttonId: Int, @StringRes label: Int) {
        findViewById<Button>(buttonId).apply {
            setText(label)
            setOnClickListener {
                val intent = Intent(this@RouterActivity, A::class.java)
                startActivity(intent)
            }
        }
    }

}
