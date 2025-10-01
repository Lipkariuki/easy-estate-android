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

    private val adminAccount = AdminAccount(
        name = "Easy Estate Admin",
        email = "admin@easyestate.com",
        password = "Admin123!"
    )

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            setLoading(true)
            delay(350)
            if (email.isBlank() || password.isBlank()) {
                emitMessage("Enter both email and password")
            } else {
                val matchedAccount = when {
                    email == adminAccount.email && password == adminAccount.password -> adminAccount
                    _uiState.value.lastCreatedAccount?.let {
                        it.email == email && it.password == password
                    } == true -> _uiState.value.lastCreatedAccount
                    else -> null
                }

                if (matchedAccount != null) {
                    emitNavigateHome(matchedAccount)
                } else {
                    emitMessage("Invalid credentials. Try admin@easyestate.com / Admin123!")
                }
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
                else -> {
                    val newAccount = AdminAccount(name, email, password)
                    _uiState.update { it.copy(lastCreatedAccount = newAccount) }
                    emitMessage(
                        message = "Account created for $name. Use the admin credentials to log in for now.",
                        navigateBackToLogin = true
                    )
                }
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

    private fun emitNavigateHome(account: AdminAccount) {
        _uiState.update {
            it.copy(
                isLoading = false,
                currentAdminName = account.name,
                event = AuthUiEvent.NavigateHome(account.name)
            )
        }
    }
}

data class AuthUiState(
    val isLoading: Boolean = false,
    val event: AuthUiEvent? = null,
    val currentAdminName: String? = "Easy Estate Admin",
    val lastCreatedAccount: AdminAccount? = null
)

sealed interface AuthUiEvent {
    data class Message(val message: String, val navigateBackToLogin: Boolean = false) : AuthUiEvent
    data class NavigateHome(val adminName: String) : AuthUiEvent
}

data class AdminAccount(
    val name: String,
    val email: String,
    val password: String
)
