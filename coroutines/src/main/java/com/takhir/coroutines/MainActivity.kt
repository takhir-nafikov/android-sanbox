package com.takhir.coroutines


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    findViewById<View>(R.id.btnRun).setOnClickListener{
      onRun()
    }

    findViewById<View>(R.id.btnCancel).setOnClickListener{
      onCancel()
    }

    findViewById<View>(R.id.btnRun2).setOnClickListener{
      onRun2()
    }
  }

  lateinit var job: Job

  // lazy
  /*private fun onRun() {
    scope.launch {
      log("onRun, start")

      job = scope.launch(start = CoroutineStart.LAZY) {
        log("coroutine, start")
        TimeUnit.MILLISECONDS.sleep(1000)
        log("coroutine, end")
      }

      log("onRun, end")
    }
  } */

  private fun onRun() {
    val job = scope.launch {
      log("parent start")
      launch {
        log("child start")
        delay(1000)
        log("child end")
      }
      log("parent end")
    }

    scope.launch {
      delay(500)
      log("parent job is active: ${job.isActive}")
      delay(1000)
      log("parent job is active: ${job.isActive}")
    }
  }

  private suspend fun getData(): String =
    suspendCoroutine {
      log("suspend function, start")
      thread {
        log("suspend function, background work")
        TimeUnit.MILLISECONDS.sleep(1000)
        it.resume("Data!")
      }
    }

  private fun onRun2() {
//    log("onRun2, start")
//    job.start()
//    log("onRun2, end")
  }

  private fun onCancel() {
    log("onCancel")
    job.cancel()
  }

  override fun onDestroy() {
    super.onDestroy()
    log("onDestroy")
    scope.cancel()
  }

  private fun log(text: String) {
    Log.d("TAG", "${formatter.format(Date())} $text [${Thread.currentThread().name}]")
  }
}
