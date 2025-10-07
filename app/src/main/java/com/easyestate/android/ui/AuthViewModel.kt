package com.easyestate.android.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.easyestate.android.data.ApiClient
import com.easyestate.android.data.LoginRequest
import com.easyestate.android.data.SignupRequest

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

            val trimmedEmail = email.trim()
            try {
                val response = ApiClient.instance.signIn(LoginRequest(email = trimmedEmail, password = password))
                if (response.isSuccessful) {
                    val tokenResponse = response.body()
                    if (tokenResponse == null) {
                        emitMessage("Login failed. Please try again.")
                        return@launch
                    }

                    if (!tokenResponse.user.active) {
                        emitMessage("Please verify your email before logging in.")
                        return@launch
                    }

                    val account = AdminAccount(name = tokenResponse.user.email, email = tokenResponse.user.email, password = "")
                    emitNavigateHome(account)
                    return@launch
                }

                val message = when (response.code()) {
                    401 -> "Invalid credentials. Please try again."
                    403 -> "Please verify your email before logging in."
                    404 -> "Login service unavailable. Please try again later."
                    else -> "Login failed: ${response.message()}"
                }
                emitMessage(message)
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
                        val request = SignupRequest(
                            email = email.trim(),
                            password = password,
                            role = mapUserRoleForApi(userRole)
                        )
                        val response = ApiClient.instance.register(request)
                        if (response.isSuccessful) {
                            val body = response.body()
                            val message = if (body?.verificationSent == true) {
                                "Account created for $firstName. Check your email to verify your account."
                            } else {
                                "Account created for $firstName. You can now log in."
                            }
                            emitMessage(message, navigateBackToLogin = true)
                        } else {
                            val message = when (response.code()) {
                                400 -> "Sign-up failed: Invalid details provided."
                                401 -> "Sign-up failed: Unauthorized request."
                                403 -> "Sign-up failed: Action not allowed."
                                409 -> "An account with this email already exists."
                                else -> "Sign-up failed: ${response.message()}"
                            }
                            emitMessage(message)
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

    private fun mapUserRoleForApi(userRole: String): String {
        val normalized = userRole.trim().lowercase()
        return when (normalized) {
            "owner/agent", "owner", "agent" -> "owner"
            "tenant", "manager" -> "manager"
            else -> "owner"
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
