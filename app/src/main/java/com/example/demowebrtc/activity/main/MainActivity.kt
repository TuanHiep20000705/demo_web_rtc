package com.example.demowebrtc.activity.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.demowebrtc.R
import com.example.demowebrtc.data.model.DataModel
import com.example.demowebrtc.data.model.DataModelType
import com.example.demowebrtc.databinding.ActivityMainBinding
import com.example.demowebrtc.service.MainService
import com.example.demowebrtc.service.MainServiceRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainRecyclerViewAdapter.Listener, MainService.Listener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private var username: String? = null
    private val mainAdapter: MainRecyclerViewAdapter by lazy {
        MainRecyclerViewAdapter(this)
    }
    @Inject
    lateinit var mainServiceRepository: MainServiceRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        username = intent.getStringExtra(USERNAME)
        viewModel.initData(username)
        binding.mainRecyclerView.adapter = mainAdapter
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.onEvent.collect {
                    handleEvent(it)
                }
            }
        }
    }

    private fun handleEvent(event: MainViewModel.Event) {
        when (event) {
            is MainViewModel.Event.GoBackToLoginScreen -> {
                finish()
            }
            is MainViewModel.Event.InitSuccess -> {
                mainAdapter.updateList(event.status)
                startMyService()
                MainService.listener = this
            }
        }
    }

    companion object {
        const val USERNAME = "username"
    }

    private fun startMyService() {
        mainServiceRepository.startService(username!!)
    }

    override fun onVideoCallClicked(username: String) {
        viewModel.sendConnectionRequest(username, true)
    }

    override fun onAudioCallClicked(username: String) {
        viewModel.sendConnectionRequest(username, false)
    }

    override fun onCallReceived(model: DataModel) {
        val isVideoCall = model.type == DataModelType.StartVideoCall
        val isVideoCallText = if (isVideoCall) "Video" else "Audio"
        binding.apply {
            incomingCallTitleTv.text = "${model.sender} is $isVideoCallText is calling you"
            incomingCallLayout.isVisible = true
            acceptButton.setOnClickListener {
                incomingCallLayout.isVisible = false
            }
            declineButton.setOnClickListener {
                incomingCallLayout.isVisible = false
            }
        }
    }
}