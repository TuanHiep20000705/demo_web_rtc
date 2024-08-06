package com.example.demowebrtc.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.demowebrtc.R
import com.example.demowebrtc.activity.main.MainActivity
import com.example.demowebrtc.constants.MainServiceActions
import com.example.demowebrtc.data.model.DataModel
import com.example.demowebrtc.data.model.DataModelType
import com.example.demowebrtc.data.model.isValid
import com.example.demowebrtc.repository.MainRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainService : Service(), MainRepository.Listener {
    private var isServiceRunning = false
    private var username: String? = null

    @Inject
    lateinit var mainRepository: MainRepository
    private lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(
            NotificationManager::class.java
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let { incomingIntent ->
            when (incomingIntent.action) {
                MainServiceActions.START_SERVICE.name -> handleStartService(incomingIntent)
            }
        }
        return START_STICKY
    }

    private fun handleStartService(incomingIntent: Intent) {
        if (!isServiceRunning) {
            isServiceRunning = true
            username = incomingIntent.getStringExtra(MainActivity.USERNAME)
            startServiceWithNotification()

            // setup my clients
            mainRepository.listener = this
            mainRepository.initFirebase()
        }
    }

    @SuppressLint("ForegroundServiceType")
    private fun startServiceWithNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "channel1", "foreground", NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
            val notification = NotificationCompat.Builder(
                this, "channel1"
            ).setSmallIcon(R.mipmap.ic_launcher)

            startForeground(1, notification.build())
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onLatestEventReceived(dataModel: DataModel) {
        if (dataModel.isValid()) {
            when (dataModel.type) {
                DataModelType.StartVideoCall,
                DataModelType.StartAudioCall -> {
                    listener?.onCallReceived(dataModel)
                }
                else -> Unit
            }
        }
    }

    interface Listener {
        fun onCallReceived(model: DataModel)
    }

    companion object {
        var listener: Listener? = null
    }
}