package com.takhir.contentprovidersmscontacts

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

  companion object {
    private val TAG = "sms_and_contacts"
    private val INBOX = "content://sms/inbox"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val toolbar = findViewById<Toolbar>(R.id.toolbar)
    setSupportActionBar(toolbar)

    val fab = findViewById<FloatingActionButton>(R.id.fab)
    fab.setOnClickListener {
      // printSmsList
      // printContactList
    }
  }

  private fun printSmsList() {
    val hasPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
    if (hasPermission !=  PackageManager.PERMISSION_GRANTED) {
      Log.w(TAG, "READ_SMS permission is not granted. You should grant it for this app")
      return
    }

    val cursor = contentResolver.query(Uri.parse(INBOX), null, null, null, null)
    if (cursor == null) {
      Log.d(TAG, "Unable to read sms!")
      return
    }

    if (cursor.moveToFirst()) {
      do {
        val data = StringBuilder()
        for (i in 0 until cursor.columnCount) {
          data.append(" ")
            .append(cursor.getColumnName(i))
            .append(" : ")
            .append(cursor.getString(i))
        }
        data.append("\n")
        Log.i(TAG, "read sms : $data")
      } while (cursor.moveToFirst())
    }

    cursor.close()
  }

  private fun printContactList() {
    val hasPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
    if (hasPermission !=  PackageManager.PERMISSION_GRANTED) {
      Log.w(TAG, "READ_SMS permission is not granted. You should grant it for this app")
      return
    }

    val cr = contentResolver
    val cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
    if (cursor == null) {
      Log.d(TAG, "Unable to read contact!")
      return
    }

    if (cursor.count > 0) {
      while (cursor.moveToNext())  {
        val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
        val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

        if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
          val phoneCursor = cr.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
            arrayOf<String>(id),
            null
          )

          while (phoneCursor!!.moveToNext()) {
            val phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

            Log.i(TAG, "Name: $name")
            Log.i(TAG, "PhoneNumber: $phoneNumber")
          }

          phoneCursor.close()
        }
      }
    }
    cursor.close()
  }
}
