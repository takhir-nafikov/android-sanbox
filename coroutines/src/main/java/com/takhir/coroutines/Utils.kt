package com.takhir.coroutines

import android.util.Log
import kotlinx.coroutines.Job
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

class Utils {
  companion object {
    private var formatter = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())

    fun coroutineLog(text: String) {
      Log.d("Coroutine lesson", "${formatter.format(Date())} $text [${Thread.currentThread().name}]")
    }

    fun contextToString(context: CoroutineContext): String =
      "Job = ${context[Job]}, Dispatcher = ${context[ContinuationInterceptor]}"
  }
}