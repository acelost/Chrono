package com.acelost.chronodemo.samples

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.acelost.chrono.Chrono
import com.acelost.chronodemo.R
import com.acelost.chronodemo.SampleActivity

class TraceFragmentTransactionSample : SampleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Chrono.start("transaction")
        val transaction = supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, DummyFragment())
        Chrono.capture("transaction", "transaction ready")
        transaction.commit()
    }

}

class DummyFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Chrono.capture("transaction", "fragment on create")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dummy, container, false)
    }

    override fun onResume() {
        super.onResume()
        Chrono.capture("transaction", "fragment is visible")
        Chrono.stop("transaction")
    }

}