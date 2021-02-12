package com.takhir.coroutines.lesson12


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.takhir.coroutines.R
import com.takhir.coroutines.UserData
import com.takhir.coroutines.Utils.Companion.contextToString
import com.takhir.coroutines.Utils.Companion.coroutineLog
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MainActivity : AppCompatActivity() {

  private val scope = CoroutineScope(Job())

  lateinit var job: Job

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)


    findViewById<View>(R.id.btnRun).setOnClickListener{
      run()
    }

    findViewById<View>(R.id.btnCancel).setOnClickListener{
    }

    findViewById<View>(R.id.btnRun2).setOnClickListener{
    }
  }


  private fun run() {
    val job = scope.launch {
      coroutineLog("parent start")
      launch {
        coroutineLog("child start")
        delay(1000)
        coroutineLog("child end")
      }
      coroutineLog("parent end")
    }

    scope.launch {
      delay(500)
      coroutineLog("parent job is active: ${job.isActive}")
      delay(1000)
      coroutineLog("parent job is active: ${job.isActive}")
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    coroutineLog("onDestroy")
    scope.cancel()
  }
}
