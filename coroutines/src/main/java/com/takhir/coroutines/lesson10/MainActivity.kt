package com.takhir.coroutines.lesson10


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
import java.util.concurrent.TimeUnit
import kotlin.coroutines.EmptyCoroutineContext

class MainActivity : AppCompatActivity() {

  private val scope = CoroutineScope(Job())

  lateinit var job: Job

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)


    findViewById<View>(R.id.btnRun).setOnClickListener{
      runV5()
    }

    findViewById<View>(R.id.btnCancel).setOnClickListener{
    }

    findViewById<View>(R.id.btnRun2).setOnClickListener{
      run2()
    }
  }


  private fun run() {
    val scope = CoroutineScope(Dispatchers.Default)
    coroutineLog("context = ${scope.coroutineContext}")
  }


  private fun runV2() {
    val scope = CoroutineScope(EmptyCoroutineContext)
    coroutineLog("scope, ${contextToString(scope.coroutineContext)}")

    scope.launch {
      coroutineLog("coroutine, ${contextToString(coroutineContext)}")
    }
  }

  private fun runV3() {
    val scope = CoroutineScope(Dispatchers.Main)
    coroutineLog("scope, ${contextToString(scope.coroutineContext)}")

    scope.launch {
      coroutineLog("coroutine, ${contextToString(coroutineContext)}")
    }
  }

  private fun runV4() {
    val scope = CoroutineScope(Job() + Dispatchers.Main)
    coroutineLog("scope, ${contextToString(scope.coroutineContext)}")

    scope.launch {
      coroutineLog("coroutine, level1, ${contextToString(coroutineContext)}")

      launch {
        coroutineLog("coroutine, level2, ${contextToString(coroutineContext)}")

        launch {
          coroutineLog("coroutine, level3, ${contextToString(coroutineContext)}")
        }
      }
    }
  }

  private fun runV5() {
    val scope = CoroutineScope(Job() + Dispatchers.Main)
    coroutineLog("scope, ${contextToString(scope.coroutineContext)}")

    scope.launch {
      coroutineLog("coroutine, level1, ${contextToString(coroutineContext)}")

      launch(Dispatchers.Default) {
        coroutineLog("coroutine, level2, ${contextToString(coroutineContext)}")

        launch {
          coroutineLog("coroutine, level3, ${contextToString(coroutineContext)}")
        }
      }
    }
  }

  private fun run2() {
    val userData = UserData(1, "name1", 10)
    val scope = CoroutineScope(Job() + Dispatchers.Default + userData)
    coroutineLog("context = ${scope.coroutineContext}")
  }

  override fun onDestroy() {
    super.onDestroy()
    coroutineLog("onDestroy")
    scope.cancel()
  }
}
