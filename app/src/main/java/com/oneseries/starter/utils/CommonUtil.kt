package com.oneseries.starter.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.provider.MediaStore
import com.oneseries.starter.R
import java.io.File
import java.io.IOException
import java.io.OutputStream

object CommonUtil {

    fun saveImg(context: Context){
        Thread {
            val fileName = "donate_wechat.png"
            val res = context.resources
            //val date = System.currentTimeMillis()
            //var imageDate = SimpleDateFormat("yyyyMMdd-HHmmss").format(Date(date))
            val image = BitmapFactory.decodeResource(res, R.drawable.donate_wechat)
            val values: ContentValues = ContentValues()
            values.put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES + File.separator + context.getString(R.string.app_name)
            )
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            values.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            values.put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis())
            values.put(MediaStore.MediaColumns.DATE_MODIFIED, System.currentTimeMillis())
            values.put(MediaStore.MediaColumns.IS_PENDING, 1)
            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            try {
                var out: OutputStream? = uri?.let { resolver.openOutputStream(it) }
                try {
                    if (!image.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                        throw IOException("Failed to compress")
                    }
                } catch (e: IOException) {

                } finally {
                    out?.close()
                }
                values.clear()
                values.put(MediaStore.MediaColumns.IS_PENDING, 0)
                values.putNull(MediaStore.MediaColumns.DATE_EXPIRES)
                uri?.let { resolver.update(it, values, null, null) }
            } catch (e: IOException) {
                uri?.let { resolver.delete(it, null) }
            }
        }.run()
    }
}