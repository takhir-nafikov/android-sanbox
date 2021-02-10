package com.takhir.coroutines.lesson8


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.takhir.coroutines.R
import com.takhir.coroutines.Utils.Companion.coroutineLog
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

  private val scope = CoroutineScope(Job())

  lateinit var job: Job

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
    }
  }

  private fun onRun() {
    coroutineLog("onRun, start")

    job = scope.launch {
      coroutineLog("cor, start")
      var x = 0
      while (x < 5 && isActive) {
//        TimeUnit.MILLISECONDS.sleep(1000)
        delay(1000)
        coroutineLog("cor, ${x++}, isActive = $isActive")
      }
      coroutineLog("cor, end")
    }

    coroutineLog("onRun, end")
  }

  private fun onCancel() {
    coroutineLog("onCancel")
    job.cancel()
  }

  override fun onDestroy() {
    super.onDestroy()
    coroutineLog("onDestroy")
    scope.cancel()
  }

}
