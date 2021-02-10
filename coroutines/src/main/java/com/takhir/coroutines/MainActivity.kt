package com.takhir.coroutines


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.takhir.coroutines.Utils.Companion.coroutineLog
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.coroutines.*

class MainActivity : AppCompatActivity() {

  private var formatter = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())

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
  }

  override fun onDestroy() {
    super.onDestroy()
    coroutineLog("onDestroy")
    scope.cancel()
  }

}
