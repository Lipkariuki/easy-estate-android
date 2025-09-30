package com.easyestate.android.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            setLoading(true)
            delay(350)
            if (email.isBlank() || password.isBlank()) {
                emitMessage("Enter both email and password")
            } else {
                emitMessage("Signed in as $email")
            }
        }
    }

    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            setLoading(true)
            delay(350)
            when {
                name.isBlank() -> emitMessage("Add your full name")
                email.isBlank() -> emitMessage("Email is required")
                password.length < 6 -> emitMessage("Password must be at least 6 characters")
                else -> emitMessage(
                    message = "Account created for $name",
                    navigateBackToLogin = true
                )
            }
        }
    }

    fun consumeEvent() {
        _uiState.update { it.copy(event = null) }
    }

    private fun emitMessage(message: String, navigateBackToLogin: Boolean = false) {
        _uiState.update {
            it.copy(
                isLoading = false,
                event = AuthUiEvent.Message(message, navigateBackToLogin)
            )
        }
    }

    private fun setLoading(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }
}

data class AuthUiState(
    val isLoading: Boolean = false,
    val event: AuthUiEvent? = null
)

sealed interface AuthUiEvent {
    data class Message(val message: String, val navigateBackToLogin: Boolean = false) : AuthUiEvent
}
