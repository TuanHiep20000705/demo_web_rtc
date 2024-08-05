package com.example.demowebrtc.activity.login

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.demowebrtc.activity.main.MainActivity
import com.example.demowebrtc.databinding.ActivityLoginBinding
import com.example.demowebrtc.utils.hasPermission
import com.example.demowebrtc.utils.isPermissionDeniedPermanently
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

//    private val recordPermissionResult =
//        registerForActivityResult(
//            ActivityResultContracts.RequestMultiplePermissions(),
//        ) { permissions ->
//            val isPermissionGranted =
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                    Environment.isExternalStorageManager()
//                } else {
//                    permissions.map { it.value }
//                        .containsAll(listOf(true, true))
//                }
//            if (isPermissionDeniedPermanently(Manifest.permission.RECORD_AUDIO)
//            ) {
//                // Do nothing, leave it alone here.
//            } else {
//                // permission granted.
//                if (isPermissionGranted) {
//                    viewModel.updateAudioRecordPermissionState(true)
//                    binding.switchRecord.isChecked = true
//                } else {
//                    AppUserManager.needToShowRationaleDialog = true
//                }
//            }
//        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.onEvent.collect {
                    handleEvent(it)
                }
            }
        }
//        FirebaseDatabase.getInstance().reference.child("name").setValue("hiep")

//        if (!hasPermission(Manifest.permission.RECORD_AUDIO)) {
//            recordPermissionResult.launch(
//                arrayOf(Manifest.permission.RECORD_AUDIO),
//            )
//        }
    }

    private fun handleEvent(event: LoginViewModel.Event) {
        when (event) {
            is LoginViewModel.Event.OnLoginClick -> {
                viewModel.login(
                    binding.usernameEt.text.toString(),
                    binding.passwordEt.text.toString()
                )
            }

            is LoginViewModel.Event.ToastSomething -> {
                Toast.makeText(this, event.content, Toast.LENGTH_LONG).show()
            }

            is LoginViewModel.Event.LoginSuccess -> {
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra(MainActivity.USERNAME, binding.usernameEt.text.toString())
                }
                startActivity(intent)
            }
        }
    }
}