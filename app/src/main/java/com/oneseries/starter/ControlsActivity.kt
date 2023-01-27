package com.oneseries.starter

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.oneseries.starter.databinding.ActivityControlsBinding
import com.oneseries.starter.utils.InstallUtil
import com.oneseries.starter.utils.ThemeUtil
import com.oneseries.starter.viewModel.ControlsViewModel

class ControlsActivity : AppCompatActivity() {
    private lateinit var viewModel: ControlsViewModel
    private lateinit var binding: ActivityControlsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityControlsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        viewModel = ViewModelProvider(this)[ControlsViewModel::class.java]

        setUIListener()
        initPreferences()
        setOnClickListener()
    }

    override fun onResume() {
        super.onResume()
        checkStatus()
    }

    private fun setOnClickListener(){
        binding.AppBarControls.setNavigationOnClickListener {
            finish()
        }
        binding.controlsLayout.controlsSummaryButtonInstall.setOnClickListener {
            goTutorial()
        }
        binding.controlsLayout.controlsSummaryButtonUninstall.setOnClickListener {
            ThemeUtil.uninstallTheme(this, viewModel.targetPackageName.value.toString())
            Toast.makeText(this,getString(R.string.toast_tips_uninstall),Toast.LENGTH_SHORT).show()
        }
        binding.controlsLayout.controlsSummaryButtonReinstall.setOnClickListener{
            InstallUtil.installAPK(this,false)
        }
        binding.controlsLayout.controlsSummaryButtonUpdate.setOnClickListener {
            InstallUtil.installAPK(this,false)
        }
        binding.controlsLayout.controlsSpecialButtonReinstall.setOnClickListener {
            InstallUtil.installAPK(this,true)
        }
        binding.controlsLayout.controlsManageButton.setOnClickListener {
            ThemeUtil.manageTheme(this)
        }
        binding.controlsLayout.controlsGeneralButtonUninstall.setOnClickListener {
            generalUninstall()
        }
    }

    private fun initPreferences(){
        viewModel.setHasSpecial(resources.getBoolean(R.bool.has_special_edition))
        viewModel.setTargetPackageName(getString(R.string.config_store_icon_pkg))
        checkStatus()
    }

    private fun setUIListener(){
        viewModel.status.observe(this, Observer {
            when(it){
                Status_Running -> {
                    binding.controlsLayout.controlsInstalledVersion.visibility = View.VISIBLE
                    //binding.controlsLayout.controlsThisVersion.visibility = View.VISIBLE
                    binding.controlsLayout.controlsSecondary.text = getString(R.string.controls_summary_secondary_running)
                    binding.controlsLayout.controlsBody.setText(R.string.controls_summary_body_running)
                    binding.controlsLayout.controlsSummaryButtonInstall.visibility = View.GONE
                    binding.controlsLayout.controlsSummaryButtonUninstall.visibility = View.VISIBLE
                    binding.controlsLayout.controlsSummaryButtonReinstall.visibility = View.VISIBLE
                    binding.controlsLayout.controlsSummaryButtonUpdate.visibility = View.GONE
                }
                Status_Update -> {
                    binding.controlsLayout.controlsInstalledVersion.visibility = View.VISIBLE
                    // binding.controlsLayout.controlsThisVersion.visibility = View.VISIBLE
                    binding.controlsLayout.controlsSecondary.text = getString(R.string.controls_summary_secondary_update)
                    binding.controlsLayout.controlsBody.setText(R.string.controls_summary_body_update)
                    binding.controlsLayout.controlsSummaryButtonInstall.visibility = View.GONE
                    binding.controlsLayout.controlsSummaryButtonUninstall.visibility = View.VISIBLE
                    binding.controlsLayout.controlsSummaryButtonReinstall.visibility = View.GONE
                    binding.controlsLayout.controlsSummaryButtonUpdate.visibility = View.VISIBLE
                }
                else -> {
                    binding.controlsLayout.controlsInstalledVersion.visibility = View.GONE
                    // binding.controlsLayout.controlsThisVersion.visibility = View.VISIBLE
                    binding.controlsLayout.controlsSecondary.text = getString(R.string.controls_summary_secondary_error)
                    binding.controlsLayout.controlsBody.setText(R.string.controls_summary_body_error)
                    binding.controlsLayout.controlsSummaryButtonInstall.visibility = View.VISIBLE
                    binding.controlsLayout.controlsSummaryButtonUninstall.visibility = View.GONE
                    binding.controlsLayout.controlsSummaryButtonReinstall.visibility = View.GONE
                    binding.controlsLayout.controlsSummaryButtonUpdate.visibility = View.GONE
                }
            }
        })
        viewModel.latestVersion.observe(this, Observer {
            binding.controlsLayout.controlsThisVersion.text = getString(R.string.controls_summary_this_version, it)
        })
        viewModel.targetPackageVersion.observe(this, Observer {
            binding.controlsLayout.controlsInstalledVersion.text = getString(R.string.controls_summary_installed_version, it)
        })
        viewModel.hasSpecial.observe(this, Observer {
            binding.controlsLayout.controlsCardSpecial.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    private fun generalUninstall(){
        var inputText = binding.controlsLayout.controlsGeneralTextField.editText?.text.toString()
        if (inputText.isNullOrEmpty()) return
        if (inputText.startsWith("http",ignoreCase = true)){
            inputText = inputText.replace(Regex("[\\s\\S]*appId="), "")
            ThemeUtil.uninstallTheme(this, inputText)
            binding.controlsLayout.controlsGeneralTextField.editText?.setText(inputText)
        }
        binding.controlsLayout.controlsGeneralTextField.clearFocus()
        Toast.makeText(this,getString(R.string.toast_tips_uninstall),Toast.LENGTH_SHORT).show()
        ThemeUtil.uninstallTheme(this, inputText)
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
        val thisVersionCode = packageManager.getPackageInfo(packageName,0).longVersionCode
        viewModel.setTargetPackageVersion(versionCode)
        viewModel.setLatestVersion(thisVersionCode)
        when (versionCode) {
            in 0..999 -> viewModel.setStatus(Status_Error)
            in 1000 until thisVersionCode -> viewModel.setStatus(Status_Update)
            else -> viewModel.setStatus(Status_Running)
        }

    }

    private fun goTutorial(){
        val intent: Intent = Intent(this@ControlsActivity,TutorialActivity::class.java)
        val pkg: String = viewModel.targetPackageName.value.toString()
        intent.putExtra("targetPackageName",pkg)
        startActivity(intent)
    }
}