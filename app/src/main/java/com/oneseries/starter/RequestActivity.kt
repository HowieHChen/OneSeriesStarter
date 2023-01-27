package com.oneseries.starter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.oneseries.starter.databinding.ActivityRequestBinding

class RequestActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRequestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setOnClickListener()
    }

    private fun setOnClickListener(){
        binding.AppBarControls.setNavigationOnClickListener {
            finish()
        }
    }
}