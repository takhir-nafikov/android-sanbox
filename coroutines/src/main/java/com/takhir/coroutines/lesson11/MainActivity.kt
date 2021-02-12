package com.takhir.coroutines.lesson11


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
      runV6()
    }

    findViewById<View>(R.id.btnCancel).setOnClickListener{
    }

    findViewById<View>(R.id.btnRun2).setOnClickListener{
      run2()
    }
  }


  private fun run() {
    val scope = CoroutineScope(Dispatchers.Default)

    repeat(6) {
      scope.launch {
        coroutineLog("coroutine $it, start")
        TimeUnit.MILLISECONDS.sleep(100)
        coroutineLog("coroutine $it, end")
      }
    }
  }

  private fun runV2() {
    val scope = CoroutineScope(Dispatchers.IO)

    repeat(6) {
      scope.launch {
        coroutineLog("coroutine $it, start")
        TimeUnit.MILLISECONDS.sleep(100)
        coroutineLog("coroutine $it, end")
      }
    }
  }

  private fun runV3() {
    val scope = CoroutineScope(
      Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    )

    repeat(6) {
      scope.launch {
        coroutineLog("coroutine $it, start")
        TimeUnit.MILLISECONDS.sleep(100)
        coroutineLog("coroutine $it, end")
      }
    }
  }

  private fun runV4() {
    val scope = CoroutineScope(Dispatchers.Main)
    coroutineLog("scope, ${contextToString(scope.coroutineContext)}")

    scope.launch {
      val data = getData()
      updateUI(data)
    }
  }

  private fun updateUI(data: String) {
    coroutineLog(data)
  }

  private fun runV5() {
    val scope = CoroutineScope(Dispatchers.Default)

    scope.launch() {
      coroutineLog("start coroutine")
      val data = getData()
      coroutineLog("end coroutine")
    }
  }

  private fun runV6() {
    val scope = CoroutineScope(Dispatchers.Unconfined)

    scope.launch() {
      coroutineLog("start coroutine")
      val data = getData()
      coroutineLog("end coroutine")
    }
  }

  private fun run2() {
    val userData = UserData(1, "name1", 10)
    val scope = CoroutineScope(Job() + Dispatchers.Default + userData)
    coroutineLog("context = ${scope.coroutineContext}")
  }

  private suspend fun getData(): String =
    suspendCoroutine {
      coroutineLog("suspend function, start")
      thread {
        coroutineLog("suspend function, background work")
        TimeUnit.MILLISECONDS.sleep(1000)
        it.resume("Data!")
      }
    }

  override fun onDestroy() {
    super.onDestroy()
    coroutineLog("onDestroy")
    scope.cancel()
  }
}
