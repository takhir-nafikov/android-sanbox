package com.takhir.coroutines.lesson14


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
      runV3()
    }

    findViewById<View>(R.id.btnCancel).setOnClickListener{
    }

    findViewById<View>(R.id.btnRun2).setOnClickListener{
      run2()
    }
  }


  private fun run() {
    scope.launch { // Coroutine_1
      try {
        launch { Integer.parseInt("a") } // Coroutine_1_1
      } catch (e: Exception) {}
      launch {} // Coroutine_1_2
    }

    scope.launch { // Coroutine_2
      launch {} // Coroutine_2_1
      launch {} // Coroutine_2_2
    }
  }

  private fun runV2() {
    val handler = CoroutineExceptionHandler { context, exception ->
      coroutineLog("$exception was handled in Coroutine_${context[CoroutineName]?.name}")
    }

    val scope = CoroutineScope(Job() + Dispatchers.Default + handler)

    scope.launch(CoroutineName("1")) {

      launch(CoroutineName("1_1")) {
        TimeUnit.MILLISECONDS.sleep(500)
        Integer.parseInt("a")
      }

      launch(CoroutineName("1_2")) {
        TimeUnit.MILLISECONDS.sleep(1000)
      }
    }

    scope.launch(CoroutineName("2")) {

      launch(CoroutineName("2_1")) {
        TimeUnit.MILLISECONDS.sleep(1000)
      }

      launch(CoroutineName("2_2")) {
        TimeUnit.MILLISECONDS.sleep(1000)
      }
    }
  }

  fun CoroutineScope.repeatIsActive() {
    repeat(5) {
      TimeUnit.MILLISECONDS.sleep(300)
      coroutineLog("Coroutine_${coroutineContext[CoroutineName]?.name} isActive $isActive")
    }
  }


  private fun runV3() {
    val handler = CoroutineExceptionHandler { context, exception ->
      coroutineLog("$exception was handled in Coroutine_${context[CoroutineName]?.name}")
    }

    val scope = CoroutineScope(Job() + Dispatchers.Default + handler)

    scope.launch(CoroutineName("1")) {
      launch(CoroutineName("1_1")) {
        TimeUnit.MILLISECONDS.sleep(1000)
        coroutineLog("exception")
        Integer.parseInt("a")
      }
      launch(CoroutineName("1_2")) { repeatIsActive() }
      repeatIsActive()
    }

    scope.launch(CoroutineName("2")) {
      launch(CoroutineName("2_1")) { repeatIsActive() }
      launch(CoroutineName("2_2")) { repeatIsActive() }
      repeatIsActive()
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
