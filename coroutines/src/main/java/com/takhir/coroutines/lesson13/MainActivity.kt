package com.takhir.coroutines.lesson13


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
    coroutineLog("onRun start")

    try {
      scope.launch {
        Integer.parseInt("a")
      }
    } catch (e: Exception) {
      coroutineLog("error $e")
    }

    coroutineLog("onRun end")
  }

  private fun runV2() {
    scope.launch {
      try {
        Integer.parseInt("a")
      } catch (e: Exception) {
        coroutineLog("exception $e")
      }
    }
  }

  private fun runV3() {
    val handler = CoroutineExceptionHandler { context, exception ->
      coroutineLog("handled $exception")
    }

    scope.launch(handler) {
      Integer.parseInt("a")
    }
  }

  private fun runV4() {
    val handler = CoroutineExceptionHandler { context, exception ->
      coroutineLog("first coroutine exception $exception")
    }

    scope.launch(handler) {
      TimeUnit.MILLISECONDS.sleep(1000)
      Integer.parseInt("a")
    }

    scope.launch {
      repeat(5) {
        TimeUnit.MILLISECONDS.sleep(300)
        coroutineLog("second coroutine isActive ${isActive}")
      }
    }
  }

  private fun runV5() {
    val handler = CoroutineExceptionHandler { context, exception ->
      coroutineLog("first coroutine exception $exception")
    }

    val scope2 = CoroutineScope(Dispatchers.Default)

    scope.launch(handler) {
      TimeUnit.MILLISECONDS.sleep(1000)
      Integer.parseInt("a")
    }

    scope2.launch {
      repeat(5) {
        TimeUnit.MILLISECONDS.sleep(300)
        coroutineLog("second coroutine isActive ${isActive}")
      }
    }
  }

  private fun runV6() {
    val handler = CoroutineExceptionHandler { context, exception ->
      coroutineLog("first coroutine exception $exception")
    }

    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default + handler)

    scope.launch {
      TimeUnit.MILLISECONDS.sleep(1000)
      Integer.parseInt("a")
    }

    scope.launch {
      repeat(5) {
        TimeUnit.MILLISECONDS.sleep(300)
        coroutineLog("second coroutine isActive ${isActive}")
      }
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
