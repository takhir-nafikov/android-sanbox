package com.takhir.coroutines.lesson18


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.takhir.coroutines.R
import com.takhir.coroutines.UserData
import com.takhir.coroutines.Utils.Companion.coroutineLog
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
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
      runV7()
    }

    findViewById<View>(R.id.btnCancel).setOnClickListener{
    }

    findViewById<View>(R.id.btnRun2).setOnClickListener{
      run2()
    }
  }

  private fun run() {
    scope.launch {
      val channel = Channel<Int>()

      launch {
        // ...
        channel.send(5)
        // ...
      }

      launch {
        // ...
        val i = channel.receive()
        coroutineLog(i.toString())
      }
    }
  }

  private fun runV2() {
    scope.launch {
      val channel = Channel<Int>()

      launch {
        delay(300)
        coroutineLog("send 5")
        channel.send(5)
        coroutineLog("send, done")
      }

      launch {
        delay(1000)
        coroutineLog("receive")
        val i = channel.receive()
        coroutineLog("receive $i, done")
      }
    }
  }

  private fun runV3() {
    scope.launch {
      val channel = Channel<Int>()

      launch {
        delay(1000)
        coroutineLog("send 5")
        channel.send(5)
        coroutineLog("send, done")
      }

      launch {
        delay(300)
        coroutineLog("receive")
        val i = channel.receive()
        coroutineLog("receive $i, done")
      }
    }
  }

  private fun runV4() {
    scope.launch {
      val channel = Channel<Int>()

      launch {
        channel.send(5)
        channel.send(6)
        channel.send(7)
        channel.close()
      }

      launch {
        for (element in channel) {
          coroutineLog(element.toString())
        }
      }
    }
  }

  private fun runV5() {
    scope.launch {
      val channel = Channel<Int>(2)

      launch {
        repeat(7) {
          delay(300)
          coroutineLog("send $it")
          channel.send(it)
        }
        coroutineLog("close")
        channel.close()
      }

      launch {
        for (element in channel) {
          coroutineLog("received $element")
          delay(1000)
        }
      }
    }
  }

  private fun runV6() {
    scope.launch {
      val channel = Channel<Int>(Channel.Factory.CONFLATED)

      launch {
        repeat(7) {
          delay(300)
          coroutineLog("send $it")
          channel.send(it)
        }
        coroutineLog("close")
        channel.close()
      }

      launch {
        for (element in channel) {
          coroutineLog("received $element")
          delay(1000)
        }
      }
    }
  }

  private fun runV7() {
    scope.launch {
      val channel = Channel<Int>(2)

      launch {
        repeat(7) {
          delay(300)
          coroutineLog("send $it")
          channel.send(it)
        }
        coroutineLog("cancel")
        channel.cancel()
      }

      launch {
        for (element in channel) {
          coroutineLog("received $element")
          delay(1000)
        }
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
