package com.example.demowebrtc.activity.call

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.demowebrtc.databinding.ActivityCallBinding
import com.example.demowebrtc.service.MainService
import com.example.demowebrtc.service.MainServiceRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CallActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCallBinding
    private lateinit var viewModel: CallViewModel

    @Inject
    lateinit var serviceRepository: MainServiceRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[CallViewModel::class.java]
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        init()
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.onEvent.collect {
                    handleEvent(it)
                }
            }
        }
    }

    private fun handleEvent(event: CallViewModel.Event) {
        when (event) {
            else -> Unit
        }
    }

    private fun init() {
        val bundle = intent.getBundleExtra("data")
        val target = bundle?.getString(TARGET) ?: ""
        val isVideoCall = bundle?.getBoolean(IS_VIDEO_CALL) ?: true
        val isCaller = bundle?.getBoolean(IS_CALLER) ?: false
        viewModel.init(target, isVideoCall, isCaller)
        binding.apply {
            if (!isVideoCall) {
                toggleCameraButton.isVisible = false
                screenShareButton.isVisible = false
                switchCameraButton.isVisible = false
            }
            MainService.remoteSurfaceView = remoteView
            MainService.localSurfaceView = localView
            serviceRepository.setupView(isVideoCall, isCaller, target)
        }
    }

    companion object {
        const val DATA_BUNDLE = "data_bundle"
        const val TARGET = "target"
        const val IS_VIDEO_CALL = "is_video_call"
        const val IS_CALLER = "is_caller"

        fun launch(context: Context, target: String, isVideoCall: Boolean, isCaller: Boolean) {
            val intent = Intent(context, CallActivity::class.java)
            val bundle = Bundle().apply {
                putString(TARGET, target)
                putBoolean(IS_VIDEO_CALL, isVideoCall)
                putBoolean(IS_CALLER, isCaller)
            }
            intent.putExtra(DATA_BUNDLE, bundle)
            context.startActivity(intent)
        }
    }
}