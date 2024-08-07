package com.example.demowebrtc.activity.call

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CallViewModel @Inject constructor(
    application: Application
): AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asLiveData()

    private val _onEvent = Channel<Event>(Channel.BUFFERED)
    val onEvent = _onEvent.receiveAsFlow()

    fun init(target: String, isVideoCall: Boolean, isCaller: Boolean) {
        _uiState.update {
            it.copy(
                target = target,
                isVideoCall = isVideoCall,
                isCaller = isCaller
            )
        }
    }

    data class UiState(
        val target: String = "",
        val isVideoCall: Boolean = true,
        val isCaller: Boolean = false
    )

    sealed class Event {

    }
}