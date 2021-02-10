package com.takhir.coroutines.lesson7


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.takhir.coroutines.R
import com.takhir.coroutines.Utils.Companion.coroutineLog
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

  private val TAG = "MainActivity"

  private val scope = CoroutineScope(Job())

  lateinit var job: Job

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)


    findViewById<View>(R.id.btnRun).setOnClickListener{
    }

    findViewById<View>(R.id.btnCancel).setOnClickListener{
    }

    findViewById<View>(R.id.btnRun2).setOnClickListener{
    }

    job = scope.launch {
      Log.d(TAG, "scope = $this")
    }
    Log.d(TAG, "onCreate: job = $job")
  }

  override fun onDestroy() {
    super.onDestroy()
    coroutineLog("onDestroy")
    scope.cancel()
  }

}
