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

    // Re-introducing the hardcoded admin account for testing
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
            if (email.isBlank() || password.isBlank()) {
                emitMessage("Enter both email and password")
                return@launch
            }

            // TODO: Replace this with your actual API call
            try {
                val response = ApiClient.instance.signIn(email = email, password = password)
                if (response.isSuccessful && response.body() != null) {
                    // For now, we'll use the email as the name. We can fetch the full user profile later.
                    val account = AdminAccount(name = email, email = email, password = "")
                    emitNavigateHome(account)
                } else {
                    // Handle unsuccessful login (e.g., wrong password)
                    emitMessage("Invalid credentials. Please try again.")
                }
            } catch (e: Exception) {
                // Handle network errors or other exceptions
                Log.e("AuthViewModel", "Sign-in failed", e)
                emitMessage("An error occurred. Please check your connection.")
            }
        }
    }

    fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        userRole: String,
        password: String
    ) {
        viewModelScope.launch {
            setLoading(true)
            when {
                firstName.isBlank() || lastName.isBlank() -> emitMessage("First and last name are required")
                email.isBlank() -> emitMessage("Email is required")
                password.length < 6 -> emitMessage("Password must be at least 6 characters")
                else -> {
                    try {
                        val request = AuthRequest(email, password, firstName, lastName, phone, userRole)
                        val response = ApiClient.instance.register(request)
                        if (response.isSuccessful) {
                            val message = "Account created for $firstName. You can now log in."
                            emitMessage(message, navigateBackToLogin = true)
                        } else {
                            emitMessage("Sign-up failed: ${response.message()}")
                        }
                    } catch (e: Exception) {
                        emitMessage("An error occurred during sign-up.")
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
    data class Message(
        val message: String,
        val navigateBackToLogin: Boolean = false
    ) : AuthUiEvent

    data class NavigateHome(val adminName: String) : AuthUiEvent
    data object NavigateToLanding : AuthUiEvent
}

data class AdminAccount(
    val name: String,
    val email: String,
    val password: String
)
