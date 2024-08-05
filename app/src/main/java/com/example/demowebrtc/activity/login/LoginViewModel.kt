package com.example.demowebrtc.activity.login

import android.app.Application
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
class LoginViewModel @Inject constructor(
    application: Application,
    private val mainRepository: MainRepository
): AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asLiveData()

    private val _onEvent = Channel<Event>(Channel.BUFFERED)
    val onEvent = _onEvent.receiveAsFlow()

    fun onClickLogin() {
        viewModelScope.launch {
            _onEvent.send(Event.OnLoginClick)
        }
    }

    fun login(username: String, password: String) {
        mainRepository.login(username, password) { isDone, reason ->
            if (!isDone) {
                viewModelScope.launch {
                    _onEvent.send(Event.ToastSomething(reason ?: "Unknown error"))
                }
            } else {
                viewModelScope.launch {
                    _onEvent.send(Event.LoginSuccess)
                }
            }
        }
    }

    data class UiState(
        val isLoginSuccess: Boolean = false
    )

    sealed class Event {
        object OnLoginClick: Event()
        data class ToastSomething(val content: String): Event()
        object LoginSuccess: Event()
    }
}