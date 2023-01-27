package com.oneseries.starter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.oneseries.starter.databinding.ActivityTutorialBinding
import com.oneseries.starter.utils.InstallUtil
import com.oneseries.starter.utils.ThemeUtil

class TutorialActivity : AppCompatActivity() {
    private lateinit var targetPackageName: String
    private lateinit var binding: ActivityTutorialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        targetPackageName = savedInstanceState?.getString("targetPackageName")?.toString()
            ?: intent.getStringExtra("targetPackageName").toString()

        binding = ActivityTutorialBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setOnClickListener()
    }

    private fun setOnClickListener(){
        binding.AppBarTutorial.setNavigationOnClickListener {
            finish()
        }
        binding.tutorialLayout.tutorialButtonGetTarget.setOnClickListener {
            ThemeUtil.getTheme(this, targetPackageName)
        }
        binding.tutorialLayout.tutorialButtonUninstallTarget.setOnClickListener {
            ThemeUtil.uninstallTheme(this, targetPackageName)
        }
        binding.tutorialLayout.tutorialButtonInstallThis.setOnClickListener {
            InstallUtil.installAPK(this, false)
        }
        binding.tutorialLayout.tutorialButtonClearCache.setOnClickListener {
            ThemeUtil.clearThemeStoreCache(this)
        }
        binding.tutorialLayout.tutorialButtonApply.setOnClickListener {
            ThemeUtil.applyTheme(this, targetPackageName)
        }
        binding.tutorialLayout.tutorialButtonReboot.setOnClickListener {
            ThemeUtil.rebootSystem(this)
        }
    }
}