package com.oneseries.starter.utils

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Debug
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import android.util.Log
import android.util.LogPrinter
import android.widget.Toast
import androidx.core.content.FileProvider
import com.oneseries.starter.R
import java.io.*

object InstallUtil {

    fun installAPK(context: Context, isSpecial: Boolean){
        val path = if (isSpecial) "special/" else ""
        val fileName = copyAPK(context, path)
        if (fileName != "" && fileName.contains(".apk", ignoreCase = true)){
            val file = File(context.getExternalFilesDir("apks"),fileName)
            val uri = FileProvider.getUriForFile(context,context.packageName+".fileprovider",file)
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(uri,"application/vnd.android.package-archive")
            context.startActivity(intent)
        }
        else{
            Toast.makeText(context,context.getString(R.string.toast_warning_noapk),Toast.LENGTH_SHORT).show()
        }
    }

    private fun copyAPK(context: Context, path: String): String{
        var filePath = File(context.getExternalFilesDir("apks").toString())
        if (!filePath.deleteRecursively()) return ""

        val assertFiles = context.assets.list(path)
        if (assertFiles != null) for (fileName in assertFiles){
            if (!fileName.endsWith(".apk",true)) continue
            var inS: InputStream? = null
            var outS: OutputStream? = null
            try {
                inS = context.assets.open(path + fileName)
                var outFile = File(context.getExternalFilesDir("apks"),fileName)
                outS = FileOutputStream(outFile)
                copyFile(inS,outS)
                //if (install) installPackage(fileName)
            }catch (e: IOException){
                //Do nothing
            }finally {
                inS?.close()
                outS?.close()
            }
            return fileName
        }
        return ""
    }

    private fun copyFile(inS:InputStream?, outS:OutputStream){
        val buffer = ByteArray(1024)
        var read: Int? = null
        while (inS?.read(buffer).also { read = it!! } != -1){
            read?.let { outS.write(buffer,0,it) }
        }
    }
}