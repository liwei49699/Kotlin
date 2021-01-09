package com.liwei.kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.liwei.kotlin.snapshot.services.ScreenShotService

class MainActivity : AppCompatActivity() {

    private val mServiceIntent: Intent by lazy {
        Intent(this,ScreenShotService::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        startService(mServiceIntent)
    }

    override fun onPause() {
        super.onPause()
        stopService(mServiceIntent)
    }

//    suspend fun fetchTwoDocs() {
//        coroutineScope {
//            launch { fetchDoc(1) }
//            async { fetchDoc(2) }
//        }
//    }
}