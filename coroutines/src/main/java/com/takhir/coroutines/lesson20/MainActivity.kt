package com.takhir.coroutines.lesson20


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.takhir.coroutines.R
import com.takhir.coroutines.UserData
import com.takhir.coroutines.Utils.Companion.coroutineLog
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
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
    }
  }

  private fun run() {
    val flow = flowOf(1,2,3).map { it * 10 }

    scope.launch {
      flow.collect {
        coroutineLog(it.toString())
      }
    }
  }

  private fun runV2() {
    val flowStrings = flow {
      emit("abc")
      emit("def")
      emit("ghi")
    }

    val upper = flow {

      flowStrings.collect {
        emit(it.toUpperCase())
      }

    }

    scope.launch {
      upper.collect {
        coroutineLog(it)
      }
    }
  }

  private fun runV3() {
    val flowStrings = flow {
      emit("abc")
      emit("def")
      emit("ghi")
    }

    scope.launch {
      val sb = StringBuilder()

      flowStrings.collect {
        sb.append(it).append(",")
      }

      val result = sb.toString()
      coroutineLog(sb.toString())
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    coroutineLog("onDestroy")
    scope.cancel()
  }
}
