package com.takhir.coroutines.lesson9


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
      onRunV5()
    }

    findViewById<View>(R.id.btnCancel).setOnClickListener{
      onCancel()
    }

    findViewById<View>(R.id.btnRun2).setOnClickListener{
      onRun2()
    }
  }

  //join
  private fun onRun() {
    coroutineLog("onRun, start")

    scope.launch {
      coroutineLog("parent cor, start")

      val j = launch {
        coroutineLog("child cor, start")
        TimeUnit.MILLISECONDS.sleep(1000)
        coroutineLog("child cor, end")
      }

      coroutineLog("parent coroutine, wait until child completes")
      j.join()

      coroutineLog("parent cor, end")
    }

    coroutineLog("onRun, end")
  }

  //parallel
  private fun onRunV2() {
    coroutineLog("onRun, start")

    scope.launch {
      coroutineLog("parent cor, start")

      val j = launch {
        TimeUnit.MILLISECONDS.sleep(1000)
      }

      val j2 = launch {
        TimeUnit.MILLISECONDS.sleep(1500)
      }

      coroutineLog("parent coroutine, wait until child completes")
      j.join()
      j2.join()

      coroutineLog("parent cor, end")
    }

    coroutineLog("onRun, end")
  }

  //lazy
  private fun onRunV3() {
    coroutineLog("onRun, start")

    job = scope.launch(start = CoroutineStart.LAZY) {
      coroutineLog("cor, start")
      TimeUnit.MILLISECONDS.sleep(1000)
      coroutineLog("cor, end")
    }

    coroutineLog("onRun, end")
  }

  //await
  private fun onRunV4() {
    scope.launch {
      coroutineLog("parent coroutine, start")

      val deferred = async() {
        coroutineLog("child coroutine, start")
        TimeUnit.MILLISECONDS.sleep(1000)
        coroutineLog("child coroutine, end")
        "async result"
      }

      coroutineLog("parent coroutine, wait until child returns result")
      val result = deferred.await()
      coroutineLog("parent coroutine, child returns: $result")

      coroutineLog("parent coroutine, end")
    }
  }

  //await parallel
  private fun onRunV5() {
    scope.launch {
      scope.launch {
        coroutineLog("parent coroutine, start")

        val data = async { getData() }
        val data2 = async { getData2() }

        coroutineLog("parent coroutine, wait until children return result")
        val result = "${data.await()}, ${ data2.await()}"
        coroutineLog("parent coroutine, children returned: $result")

        coroutineLog("parent coroutine, end")
      }
    }
  }

  private fun onRun2() {
    coroutineLog("onRun2, start")
    job.start()
    coroutineLog("onRun2, end")
  }

  private fun onCancel() {
    coroutineLog("onCancel")
    job.cancel()
  }

  private suspend fun getData(): String {
    delay(1000)
    return "data"
  }

  private suspend fun getData2(): String {
    delay(1500)
    return "data2"
  }

  override fun onDestroy() {
    super.onDestroy()
    coroutineLog("onDestroy")
    scope.cancel()
  }
}
