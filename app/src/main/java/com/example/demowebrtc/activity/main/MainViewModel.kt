package com.example.demowebrtc.activity.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.demowebrtc.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val mainRepository: MainRepository
): AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asLiveData()

    private val _onEvent = Channel<Event>(Channel.BUFFERED)
    val onEvent = _onEvent.receiveAsFlow()

    fun initData(username: String?) {
        if (username == null) {
            viewModelScope.launch {
                _onEvent.send(Event.GoBackToLoginScreen)
            }
        } else {
            subscribeObservers()
        }
    }

    private fun subscribeObservers() {
        mainRepository.observeUserStatus {
            Log.e(">>>>", "subscribe: $it")
            viewModelScope.launch {
                _onEvent.send(Event.InitSuccess(it))
            }
        }
    }

    fun sendConnectionRequest(username: String, isVideoCall: Boolean) {
        mainRepository.sendConnectionRequest(username, isVideoCall) {

        }
    }

    data class UiState(
        val isLoadingSuccess: Boolean = false
    )

    sealed class Event {
        object GoBackToLoginScreen: Event()
        data class InitSuccess(val status: List<Pair<String, String>>): Event()
    }
}