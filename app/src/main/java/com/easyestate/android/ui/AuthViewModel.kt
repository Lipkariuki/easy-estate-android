package com.easyestate.android.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.easyestate.android.data.ApiClient
import com.easyestate.android.data.AuthRequest

class AuthViewModel : ViewModel() {

    private val apiService = ApiClient.instance

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            setLoading(true)
            if (email.isBlank() || password.isBlank()) {
                emitMessage("Enter both email and password")
                return@launch
            }

            try {
                // Step 1: Login to get the token
                val tokenResponse = apiService.login(email = email, password = password)
                val token = "Bearer ${tokenResponse.accessToken}"

                // Step 2: Use the token to get user info
                val user = apiService.getCurrentUser(token)

                // NOTE: Your FastAPI user model doesn't have a 'name'.
                // We'll use the email for now. You could add a name field to your backend later!
                emitNavigateHome(user.email)

            } catch (e: Exception) {
                Log.e("AuthViewModel", "Sign-in failed", e)
                // In a real app, you'd parse the error for a better message
                emitMessage("Login failed. Please check your credentials.")
            }
        }
    }

    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            setLoading(true)
            when {
                name.isBlank() -> emitMessage("Add your full name")
                email.isBlank() -> emitMessage("Email is required")
                password.length < 6 -> emitMessage("Password must be at least 6 characters")
                else -> {
                    try {
                        // NOTE: The 'name' field is not part of your FastAPI UserCreate schema, so it's ignored here.
                        val request = AuthRequest(email = email, password = password)
                        apiService.register(request)
                        val message = "Account created for $email. You can now log in."
                        emitMessage(message, navigateBackToLogin = true)
                    } catch (e: Exception) {
                        Log.e("AuthViewModel", "Sign-up failed", e)
                        emitMessage("Sign up failed. The email might already be in use.")
                    }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.update { it.copy(event = AuthUiEvent.NavigateToLanding) }
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

    private fun emitNavigateHome(adminName: String) {
        _uiState.update {
            it.copy(
                isLoading = false,
                currentAdminName = adminName,
                event = AuthUiEvent.NavigateHome(adminName)
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
    data object NavigateToLanding : AuthUiEvent
}

data class AdminAccount(
    val name: String,
    val email: String,
    val password: String
)
