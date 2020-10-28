package com.takhir.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.junit.Test

class Scope {
  private val TAG = "Scope"

  @Test
  fun firstScope() {
    val scope = CoroutineScope(Job())

    val job = scope.launch {
      System.out.println(this.toString());
    }

    System.out.println(job)

//    Thread.sleep(1000)
  }
}