package com.example.demowebrtc.service

import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.demowebrtc.activity.main.MainActivity
import com.example.demowebrtc.constants.MainServiceActions
import javax.inject.Inject

class MainServiceRepository @Inject constructor(
    private val context: Context
){
    private fun startServiceIntent(intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

    fun startService(username: String) {
//        CoroutineScope(Dispatchers.Main).launch {
//
//        }
        val intent = Intent(context, MainService::class.java)
        intent.putExtra(MainActivity.USERNAME, username)
        intent.action = MainServiceActions.START_SERVICE.name
        startServiceIntent(intent)
    }
}