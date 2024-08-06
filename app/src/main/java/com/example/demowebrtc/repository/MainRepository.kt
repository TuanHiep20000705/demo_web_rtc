package com.example.demowebrtc.repository

import com.example.demowebrtc.data.model.DataModel
import com.example.demowebrtc.data.model.DataModelType
import com.example.demowebrtc.firebase_client.FirebaseClient
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val firebaseClient: FirebaseClient
) {
    var listener: Listener? = null

    fun login(username: String, password: String, isDone: (Boolean, String?) -> Unit) {
        firebaseClient.login(username, password, isDone)
    }

    fun observeUserStatus(status: (List<Pair<String, String>>) -> Unit) {
        firebaseClient.observeUserStatus { stt ->
            status(stt)
        }
    }

    fun initFirebase() {
        firebaseClient.subscribeForLatestEvent(object : FirebaseClient.Listener {
            override fun onLatestEventReceived(event: DataModel) {
                listener?.onLatestEventReceived(event)
                when (event.type) {
                    else -> Unit
                }
            }
        })
    }

    fun sendConnectionRequest(target: String, isVideoCall: Boolean, success: (Boolean) -> Unit) {
        firebaseClient.sendMessageToOtherClient(
            DataModel(
                type = if (isVideoCall) DataModelType.StartVideoCall else DataModelType.StartAudioCall,
                target = target
            ), success
        )
    }

    interface Listener {
        fun onLatestEventReceived(dataModel: DataModel)
    }
}