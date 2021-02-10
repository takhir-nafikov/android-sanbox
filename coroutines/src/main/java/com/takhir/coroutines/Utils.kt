package com.takhir.coroutines

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class Utils {
  companion object {
    private var formatter = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())

    fun coroutineLog(text: String) {
      Log.d("TAG", "${formatter.format(Date())} $text [${Thread.currentThread().name}]")
    }
  }
}