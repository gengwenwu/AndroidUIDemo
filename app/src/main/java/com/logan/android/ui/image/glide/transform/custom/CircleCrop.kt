package com.logan.android.ui.image.glide.transform.custom

import android.graphics.*
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest


/**
 * desc: 自定义Transformation <br/>
 *      步骤：
 *          1， 继承BitmapTransformation；
 *          2， 重写 transform()函。
 *
 * time: 2020/6/16 10:16 AM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */
class CircleCrop : BitmapTransformation() { // 关键点1：继承BitmapTransformation

    companion object {
        // id 建议以包目录 + 文件名称，避免重复
        private const val ID = "com.logan.android.ui.image.glide.transform.custom.CircleCrop"
        private val ID_BYTES = ID.toByteArray(Key.CHARSET)
    }

    override fun hashCode() = ID.hashCode()

    override fun equals(other: Any?) = other is CircleCrop

    override fun updateDiskCacheKey(messageDigest: MessageDigest) = messageDigest.update(ID_BYTES)


    // 关键点二：重写 transform()函数
    override fun transform(
        pool: BitmapPool, // Glide中的一个Bitmap缓存池，用于对Bitmap对象进行重用，否则每次图片变换都重新创建Bitmap对象将会非常消耗内存。
        toTransform: Bitmap, // 原始图片的Bitmap对象，就是要对它来进行图片变换。
        outWidth: Int, // 图片变换后的宽度
        outHeight: Int // 图片变换后的高度， 其实outWidth、和outHeight 就是override()方法中传入的宽和高的值
    ): Bitmap {
        val diameter = Math.min(toTransform.width, toTransform.height)

        val toReuse = pool[outWidth, outHeight, Bitmap.Config.ARGB_8888]
        val result: Bitmap
        result = toReuse ?: Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888)

        val dx = (toTransform.width - diameter) / 2
        val dy = (toTransform.height - diameter) / 2

        val canvas = Canvas(result)
        val paint = Paint()
        val shader = BitmapShader(
            toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP
        )

        if (dx != 0 || dy != 0) {
            val matrix = Matrix()
            matrix.setTranslate((-dx).toFloat(), (-dy).toFloat())
            shader.setLocalMatrix(matrix)
        }

        paint.shader = shader
        paint.isAntiAlias = true
        val radius = diameter / 2f
        canvas.drawCircle(radius, radius, radius, paint)
        canvas.setBitmap(null)

        return result
    }

}