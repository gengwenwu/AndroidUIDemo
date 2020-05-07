package com.logan.android.ui.base

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {


    inline fun <reified T : AppCompatActivity> Context.startActivity() {
        val intent = Intent(this, T::class.java)
        startActivity(intent)
    }

}
