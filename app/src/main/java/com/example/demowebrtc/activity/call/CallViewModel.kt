package com.example.demowebrtc.activity.call

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class CallViewModel @Inject constructor(
    application: Application
): AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asLiveData()

    private val _onEvent = Channel<Event>(Channel.BUFFERED)
    val onEvent = _onEvent.receiveAsFlow()

    data class UiState(
        val isLoadingSuccess: Boolean = false
    )

    sealed class Event {

    }
}