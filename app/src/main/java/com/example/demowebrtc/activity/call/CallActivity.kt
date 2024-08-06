package com.example.demowebrtc.activity.call

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.demowebrtc.databinding.ActivityCallBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CallActivity: AppCompatActivity() {
    private lateinit var binding: ActivityCallBinding
    private lateinit var viewModel: CallViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[CallViewModel::class.java]
        binding.lifecycleOwner = this
        binding.executePendingBindings()
    }

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, CallActivity::class.java)
            context.startActivity(intent)
        }
    }
}