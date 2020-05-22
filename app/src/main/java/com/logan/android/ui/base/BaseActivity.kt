package com.logan.android.ui.base

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {


    inline fun <reified T : AppCompatActivity> Context.startActivity() {
        val intent = Intent(this, T::class.java)
        startActivity(intent)
    }

    fun showMsg(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}
