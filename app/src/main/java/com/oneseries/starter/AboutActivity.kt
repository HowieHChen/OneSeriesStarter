package com.oneseries.starter

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.oneseries.starter.databinding.ActivityAboutBinding
import com.oneseries.starter.utils.CommonUtil

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initView()
        setOnClickListener()
    }

    private fun initView(){
        //val donateAlipay = resources.getBoolean(R.bool.config_pay_with_alipay)
        //val donateWechat = resources.getBoolean(R.bool.config_pay_with_wechat)
        val feedbackQQ = resources.getBoolean(R.bool.config_feedback_with_qq)
        //binding.aboutLayout.aboutDonateCard.visibility = if (donateAlipay || donateWechat) View.VISIBLE else View.GONE
        //binding.aboutLayout.aboutDonateButtonAlipay.visibility = if (donateAlipay) View.VISIBLE else View.GONE
        //binding.aboutLayout.aboutDonateButtonWechat.visibility = if (donateWechat) View.VISIBLE else View.GONE
        binding.aboutLayout.aboutFeedbackButtonQq.visibility = if (feedbackQQ) View.VISIBLE else View.GONE
    }

    private fun setOnClickListener(){
        binding.AppBarAbout.setNavigationOnClickListener {
            finish()
        }
        binding.aboutLayout.aboutIconPackButtonRate.setOnClickListener {
            openURL("market://details?id=$packageName")
        }
        binding.aboutLayout.aboutAuthorButtonEmail.setOnClickListener {
            sendEmail(getString(R.string.author_email))
        }
        binding.aboutLayout.aboutAuthorButtonPage.setOnClickListener {
            openURL(getString(R.string.author_page_url))
        }
        binding.aboutLayout.aboutFeedbackButtonMarket.setOnClickListener {
            openURL("market://details?id=$packageName")
        }
        binding.aboutLayout.aboutFeedbackButtonQq.setOnClickListener {
            joinQQGroup()
        }
        binding.aboutLayout.aboutDonateButtonAlipay.setOnClickListener {
            openAlipay()
        }
        binding.aboutLayout.aboutDonateButtonWechat.setOnClickListener {
            openWechat()
        }
        binding.aboutLayout.aboutDashboardButtonAuthor.setOnClickListener {
            openURL(getString(R.string.url_dashboard_author))
        }
        binding.aboutLayout.aboutDashboardButtonSourcecode.setOnClickListener {
            openURL(getString(R.string.url_dashboard_opensource))
        }
    }

    private fun openURL(sUrl: String){
        val intent = Intent()
        intent.data = Uri.parse(sUrl)
        intent.action = Intent.ACTION_VIEW
        startActivity(intent)
    }

    private fun sendEmail(mailAddr: String){
        val intent = Intent()
        intent.data = Uri.parse("mailto:${mailAddr}")
        intent.action = Intent.ACTION_SENDTO
        startActivity(intent)
    }

    private fun joinQQGroup(){
        if (!isExist("com.tencent.mobileqq")) {
            Toast.makeText(this,getString(R.string.toast_warning_noqq), Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent()
        intent.data = Uri.parse(getString(R.string.feedback_qqgroup_url) + getString(R.string.feedback_qqgroup_key))
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
        } catch (e: Exception) {
            // 未安装手Q或安装的版本不支持
            Toast.makeText(this,getString(R.string.toast_warning_noqq), Toast.LENGTH_SHORT).show()
        }
    }

    private fun openAlipay(){
        if (!isExist("com.eg.android.AlipayGphone")) {
            Toast.makeText(this,getString(R.string.toast_warning_noalipay), Toast.LENGTH_SHORT).show()
            return
        }
        val tUrl = getString(R.string.donate_url_alipay)
        val intent = Intent()
        intent.data = Uri.parse("alipayqr://platformapi/startapp?saId=10000007&qrcode=$tUrl")
        intent.action = Intent.ACTION_VIEW
        try {
            startActivity(intent)
        }catch (e: Exception){
            Toast.makeText(this,getString(R.string.toast_warning_noalipay), Toast.LENGTH_SHORT).show()
        }
    }

    private fun openWechat(){
        if (!isExist("com.eg.android.AlipayGphone")) {
            Toast.makeText(this,getString(R.string.toast_warning_nowechat), Toast.LENGTH_SHORT).show()
            return
        }
        CommonUtil.saveImg(this)
        Toast.makeText(this,getString(R.string.toast_tips_openwechat),Toast.LENGTH_SHORT).show()
        try {
            Intent(Intent.ACTION_MAIN).apply {
                putExtra("LauncherUI.From.Scaner.Shortcut", true)
                addCategory(Intent.CATEGORY_LAUNCHER)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                component = ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI")
                startActivity(this)
            }
        }catch (e: Exception){
            Toast.makeText(this,getString(R.string.toast_warning_nowechat), Toast.LENGTH_SHORT).show()
        }
    }

    private fun isExist(pkg: String): Boolean{
        return try{
            val pkgInfo = packageManager.getPackageInfo(pkg, 0)
            pkgInfo != null
        } catch (e: PackageManager.NameNotFoundException){
            false
        }
    }
}