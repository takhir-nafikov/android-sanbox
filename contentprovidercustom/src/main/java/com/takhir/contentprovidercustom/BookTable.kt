package com.takhir.contentprovidercustom

import android.database.sqlite.SQLiteDatabase
import android.util.Log

class BookTable {
  companion object {
    val TABLE_BOOK = "book"
    val COLUMN_ID = "_id"
    val COLUMN_NAME = "name"
    val COLUMN_ISBN = "isbn"
    val COLUMN_DESCRIPTION = "description"

    private val DATABASE_CREATE = "create table " +
        "$TABLE_BOOK($COLUMN_ID integer primary key autoincrement, " +
        "$COLUMN_NAME text not null, " +
        "$COLUMN_ISBN text not null, " +
        "$COLUMN_DESCRIPTION text not null);"

    @JvmStatic
    fun onCreate(database: SQLiteDatabase) {
      database.execSQL(DATABASE_CREATE)
    }

    @JvmStatic
    fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
      Log.w(BookTable::class.java.name, "Upgrade from $oldVersion to $newVersion")
      database.execSQL("DROP TABLE IF EXISTS $TABLE_BOOK")
      onCreate(database)
    }
  }
}