package com.logan.android.ui.base

import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexboxLayout
import com.logan.android.ui.entity.ButtonModel
import com.logan.android.ui.tool.dp2px

abstract class BaseActivity : AppCompatActivity() {


    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)

        // 清除图片缓存
        when (level) {
            //
            Application.TRIM_MEMORY_BACKGROUND ->
                Glide.get(this).clearMemory()
            //
            Application.TRIM_MEMORY_MODERATE, Application.TRIM_MEMORY_COMPLETE ->
                Glide.get(this).trimMemory(level)
        }
    }

    inline fun <reified T : AppCompatActivity> Context.startActivity() {
        val intent = Intent(this, T::class.java)
        startActivity(intent)
    }

    fun showMsg(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }


    //==============

    fun showButtons(
        context: Context, buttonsContainer: ViewGroup, vararg buttons: ButtonModel
    ) {
        buttons.forEach {
            buttonsContainer.addView(createButton(context, it))
        }
    }

    private fun createButton(
        context: Context, buttonModel: ButtonModel
    ): Button {
        return Button(context).apply {
            isAllCaps = false
            text = buttonModel.buttonText

            layoutParams = FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT
            ).also { it.marginStart = dp2px(context, 5f) }

            setOnClickListener(buttonModel.onClickListener)
        }
    }

}