package com.oneseries.starter

import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.oneseries.starter.databinding.ActivityMainBinding
import com.oneseries.starter.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setUIListener()
        initPreferences()
        setOnClickListener()
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.isSamsung.value == true){
            checkStatus()
        }
    }
    private fun initPreferences(){
        if (android.os.Build.BRAND == "samsung" || android.os.Build.BRAND == "galaxy"){
            binding.cardsSamsung.cardsSamsungLayout.visibility = View.VISIBLE
            viewModel.setIsSamsung(true)
            viewModel.setTargetPackageName(getString(R.string.config_store_icon_pkg))
            checkStatus()
        }
        else{
            viewModel.setIsSamsung(true)
            binding.cardsSamsung.cardsSamsungLayout.visibility = View.VISIBLE
        }

    }

    private fun setOnClickListener(){
        binding.cardsSamsung.cardStatusTutorialButton.setOnClickListener {
            goActivity(NavTag_Tutorial)
        }
        binding.cardsSamsung.cardControls.setOnClickListener {
            goActivity(NavTag_Controls)
        }
        binding.cardsSamsung.cardTutorial.setOnClickListener {
            goActivity(NavTag_Tutorial)
        }
        binding.cardsCommon.cardDashboard.setOnClickListener {
            goActivity(NavTag_Dashboard)
        }
        binding.cardsCommon.cardRequest.setOnClickListener {
            goActivity(NavTag_Request)
        }
        binding.cardsCommon.cardAbout.setOnClickListener {
            goActivity(NavTag_About)
        }
    }

    private fun setUIListener(){
        viewModel.status.observe(this, Observer {
            when(it){
                Status_Running ->{
                    binding.cardsSamsung.cardTutorial.visibility = View.VISIBLE
                    binding.cardsSamsung.cardStatusIc.setImageResource(R.drawable.ic_outline_check_circle_24)
                    binding.cardsSamsung.cardStatusIc.imageTintList = ColorStateList.valueOf(getColor(R.color.card_status_ic_running_foreground))
                    binding.cardsSamsung.cardStatusIc.backgroundTintList = ColorStateList.valueOf(getColor(R.color.card_status_ic_running_background))
                    binding.cardsSamsung.cardStatusTitle.text = getString(R.string.card_status_running_title)
                    binding.cardsSamsung.cardStatusSecondary.text = getString(R.string.card_status_running_secondary, viewModel.targetPackageVersion.value)
                    binding.cardsSamsung.cardStatusInstructions.visibility = View.GONE
                    binding.cardsSamsung.cardStatusTutorialButton.visibility = View.GONE
                }
                Status_Update ->{
                    binding.cardsSamsung.cardTutorial.visibility = View.VISIBLE
                    binding.cardsSamsung.cardStatusIc.setImageResource(R.drawable.ic_outline_check_circle_24)
                    binding.cardsSamsung.cardStatusIc.imageTintList = ColorStateList.valueOf(getColor(R.color.card_status_ic_running_foreground))
                    binding.cardsSamsung.cardStatusIc.backgroundTintList = ColorStateList.valueOf(getColor(R.color.card_status_ic_running_background))
                    binding.cardsSamsung.cardStatusTitle.text = getString(R.string.card_status_running_title)
                    binding.cardsSamsung.cardStatusSecondary.text = getString(R.string.card_status_running_secondary, viewModel.targetPackageVersion.value)
                    binding.cardsSamsung.cardStatusInstructions.visibility = View.GONE
                    binding.cardsSamsung.cardStatusTutorialButton.visibility = View.GONE
                }
                else ->{
                    binding.cardsSamsung.cardTutorial.visibility = View.GONE
                    binding.cardsSamsung.cardStatusIc.setImageResource(R.drawable.ic_outline_error_outline_24)
                    binding.cardsSamsung.cardStatusIc.imageTintList = ColorStateList.valueOf(getColor(R.color.card_status_ic_error_foreground))
                    binding.cardsSamsung.cardStatusIc.backgroundTintList = ColorStateList.valueOf(getColor(R.color.card_status_ic_error_background))
                    binding.cardsSamsung.cardStatusTitle.text = getString(R.string.card_status_error_title)
                    binding.cardsSamsung.cardStatusSecondary.text = getString(R.string.card_status_error_secondary)
                    binding.cardsSamsung.cardStatusInstructions.visibility = View.VISIBLE
                    binding.cardsSamsung.cardStatusTutorialButton.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun checkStatus(){
        if (viewModel.targetPackageName.value.isNullOrBlank())
            viewModel.setTargetPackageName(getString(R.string.config_store_icon_pkg))
        val pkg: String = viewModel.targetPackageName.value.toString()
        var versionCode:Long = 0
        try {
            val pkgInfo = packageManager.getPackageInfo(pkg,0)
            if (pkgInfo != null) {
                versionCode = pkgInfo.longVersionCode
            }
        }catch (e: PackageManager.NameNotFoundException){
            versionCode = 0
        }
        viewModel.setTargetPackageVersion(versionCode)
        val thisVersionCode = packageManager.getPackageInfo(packageName,0).longVersionCode
        when (versionCode) {
            in 0..999 -> viewModel.setStatus(Status_Error)
            in 1000 until thisVersionCode -> viewModel.setStatus(Status_Update)
            else -> viewModel.setStatus(Status_Running)
        }
    }

    private fun goActivity(tag: Int){
        val intent: Intent
        when(tag){
            NavTag_Tutorial -> {
                intent = Intent(this@MainActivity,TutorialActivity::class.java)
                val pkg: String = viewModel.targetPackageName.value.toString()
                intent.putExtra("targetPackageName",pkg)
            }
            NavTag_Controls -> {
                intent = Intent(this@MainActivity,ControlsActivity::class.java)
            }
            NavTag_Dashboard -> {
                intent = Intent(this@MainActivity,DashboardActivity::class.java)
            }
            NavTag_Request -> {
                intent = Intent(this@MainActivity,RequestActivity::class.java)
            }
            NavTag_About -> {
                intent = Intent(this@MainActivity,AboutActivity::class.java)
            }
            else -> {
                return
            }
        }
        startActivity(intent)
    }

    companion object {
        private const val NavTag_Tutorial = 0
        private const val NavTag_Controls = 1
        private const val NavTag_Dashboard = 2
        private const val NavTag_Request = 3
        private const val NavTag_About = 4
    }
}