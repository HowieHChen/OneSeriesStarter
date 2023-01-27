package com.oneseries.starter.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import com.oneseries.starter.R

object ThemeUtil {
    private const val themeStorePkg: String = "com.samsung.android.themestore"

    fun isExist(context: Context, pkg: String): Boolean{
        return try{
            val pkgInfo = context.packageManager.getPackageInfo(pkg,0)
            pkgInfo != null
        } catch (e: PackageManager.NameNotFoundException){
            false
        }
    }
    fun manageTheme(context: Context){
        if (isExist(context, themeStorePkg)){
            val intent = Intent()
            intent.data = Uri.parse("themestore://MyTheme")
            intent.action = Intent.ACTION_VIEW
            intent.putExtra("contentType",3)
            context.startActivity(intent)
        } else{
            Toast.makeText(context, context.getString(R.string.toast_theme_store_uninstalled), Toast.LENGTH_SHORT).show()
            openURL(context,"samsungapps://apps.samsung.com/appquery/appDetail.as?appId=com.samsung.android.themestore")
        }
    }
    fun uninstallTheme(context: Context, pkg: String){
        val packageUri = Uri.parse("package:$pkg")
        val intent = Intent(Intent.ACTION_DELETE, packageUri)
        context.startActivity(intent)
    }
    fun applyTheme(context: Context, pkg: String){
        if (isExist(context, pkg)){
            val intent = Intent()
            intent.data = Uri.parse("themestore://LocalProductDetail")
            intent.action = Intent.ACTION_VIEW
            intent.putExtra("contentType",3)
            intent.putExtra("packageName",pkg)
            context.startActivity(intent)
        } else{
            Toast.makeText(context,context.getString(R.string.toast_theme_uninstalled),Toast.LENGTH_SHORT).show()
            //openURL(context,"http://apps.samsung.com/theme/ProductDetail.as?appId=$pkg")
        }
    }
    fun getTheme(context: Context, pkg: String){
        if (isExist(context, themeStorePkg)){
            Toast.makeText(context,context.getString(R.string.toast_warning_dontapply), Toast.LENGTH_SHORT).show()
            openURL(context,"themestore://ProductDetail?appId=$pkg")
        } else{
            Toast.makeText(context,context.getString(R.string.toast_theme_store_uninstalled), Toast.LENGTH_SHORT).show()
            openURL(context,"samsungapps://apps.samsung.com/appquery/appDetail.as?appId=com.samsung.android.themestore")
        }
    }
    fun clearThemeStoreCache(context: Context){
        if (isExist(context, themeStorePkg)){
            Toast.makeText(context, context.getString(R.string.toast_tips_clear_cache),Toast.LENGTH_SHORT).show()
            val intent = Intent()
            intent.data = Uri.parse("package:$themeStorePkg")
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            context.startActivity(intent)
        } else{
            Toast.makeText(context, context.getString(R.string.toast_theme_store_uninstalled), Toast.LENGTH_SHORT).show()
            openURL(context,"samsungapps://apps.samsung.com/appquery/appDetail.as?appId=com.samsung.android.themestore")
        }
    }
    fun rebootSystem(context: Context){
        val intent = Intent()
        intent.setClassName("com.android.systemui","com.android.systemui.indexsearch.DetailPanelLaunchActivity")
        context.startActivity(intent)
        Toast.makeText(context,context.getString(R.string.toast_tips_reboot), Toast.LENGTH_SHORT).show()
    }
    private fun openURL(context: Context, sUrl: String){
        val intent = Intent()
        intent.data = Uri.parse(sUrl)
        intent.action = Intent.ACTION_VIEW
        context.startActivity(intent)
    }
}