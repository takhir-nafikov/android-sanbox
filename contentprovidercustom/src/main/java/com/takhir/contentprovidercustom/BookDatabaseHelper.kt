package com.takhir.contentprovidercustom

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BookDatabaseHelper(
  context: Context
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

  companion object {
    val DATABASE_NAME = "booktable.db"
    val DATABASE_VERSION = 1
  }

  override fun onCreate(db: SQLiteDatabase?) {
    BookTable.onCreate(db!!)
  }

  override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    BookTable.onUpgrade(db!!, oldVersion, newVersion)
  }
}