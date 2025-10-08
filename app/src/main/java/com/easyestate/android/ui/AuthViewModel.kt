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
import com.easyestate.android.data.TenantCreateRequest
import com.easyestate.android.data.PropertyCreateRequest

class AuthViewModel : ViewModel() {

    private val dobRegex = Regex("""^\d{4}-\d{2}-\d{2}$""")

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

                    ApiClient.updateAuthToken(tokenResponse.accessToken)
                    _uiState.update {
                        it.copy(
                            authToken = tokenResponse.accessToken,
                            currentUserRole = tokenResponse.user.role
                        )
                    }

                    emitNavigateHome(tokenResponse.user.email)
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

    fun addProperty(
        name: String,
        propertyType: String,
        addressLine1: String,
        city: String,
        location: String,
        notes: String
    ) {
        viewModelScope.launch {
            setLoading(true)
            val trimmedName = name.trim()
            val trimmedAddress = addressLine1.trim()
            val trimmedCity = city.trim()
            val trimmedLocation = location.trim()
            val trimmedNotes = notes.trim()
            val trimmedType = propertyType.trim()
            val token = _uiState.value.authToken
            when {
                trimmedName.isBlank() -> {
                    emitMessage("Property name is required")
                    return@launch
                }
                trimmedType.isBlank() -> {
                    emitMessage("Select a property type")
                    return@launch
                }
                token.isNullOrBlank() -> {
                    emitMessage("Session expired. Please sign in again.")
                    return@launch
                }
                else -> {
                    try {
                        val request = PropertyCreateRequest(
                            name = trimmedName,
                            propertyType = trimmedType,
                            addressLine1 = trimmedAddress.takeIf { it.isNotEmpty() },
                            city = trimmedCity.takeIf { it.isNotEmpty() },
                            location = trimmedLocation.takeIf { it.isNotEmpty() },
                            imageUrl = null,
                            notes = trimmedNotes.takeIf { it.isNotEmpty() }
                        )
                        val response = ApiClient.instance.createProperty(request)
                        if (response.isSuccessful) {
                            val property = response.body()
                            val propertyName = property?.name ?: trimmedName
                            emitPropertyAdded("Property $propertyName created successfully.")
                        } else {
                            if (response.code() == 401) {
                                ApiClient.updateAuthToken(null)
                                _uiState.update { it.copy(authToken = null, currentUserRole = null) }
                            }
                            val message = when (response.code()) {
                                400 -> "Failed to add property: Invalid details provided."
                                401 -> "You are not authorized. Please sign in again."
                                403 -> "Your account is not permitted to add properties."
                                else -> "Failed to add property: ${response.message()}"
                            }
                            emitMessage(message)
                        }
                    } catch (e: Exception) {
                        Log.e("AuthViewModel", "Add property failed", e)
                        emitMessage("An error occurred while adding the property.")
                    }
                }
            }
        }
    }

    fun addTenant(
        fullName: String,
        phone: String,
        email: String,
        dob: String,
        gender: String,
        occupation: String
    ) {
        viewModelScope.launch {
            setLoading(true)
            val trimmedName = fullName.trim()
            val trimmedPhone = phone.trim()
            val trimmedEmail = email.trim()
            val trimmedDob = dob.trim()
            val trimmedOccupation = occupation.trim()
            val token = _uiState.value.authToken
            when {
                trimmedName.isBlank() -> {
                    emitMessage("Full name is required")
                    return@launch
                }
                token.isNullOrBlank() -> {
                    emitMessage("Session expired. Please sign in again.")
                    return@launch
                }
                trimmedDob.isNotEmpty() && !dobRegex.matches(trimmedDob) -> {
                    emitMessage("Date of birth must be in format YYYY-MM-DD.")
                    return@launch
                }
                else -> {
                    try {
                        val request = TenantCreateRequest(
                            fullName = trimmedName,
                            email = trimmedEmail.takeIf { it.isNotEmpty() },
                            phone = trimmedPhone.takeIf { it.isNotEmpty() },
                            idNumber = null,
                            dateOfBirth = trimmedDob.takeIf { it.isNotEmpty() },
                            gender = mapGenderForApi(gender),
                            occupation = trimmedOccupation.takeIf { it.isNotEmpty() },
                            emergencyContactName = null,
                            emergencyContactPhone = null,
                            notes = null
                        )
                        val response = ApiClient.instance.createTenant(request)
                        if (response.isSuccessful) {
                            val created = response.body()
                            val tenantName = created?.fullName ?: trimmedName
                            emitTenantAdded("Tenant $tenantName added successfully.")
                        } else {
                            if (response.code() == 401) {
                                ApiClient.updateAuthToken(null)
                                _uiState.update { it.copy(authToken = null, currentUserRole = null) }
                            }
                            val message = when (response.code()) {
                                400 -> "Failed to add tenant: Invalid details provided."
                                401 -> "You are not authorized. Please sign in again."
                                403 -> "Your account is not permitted to add tenants."
                                else -> "Failed to add tenant: ${response.message()}"
                            }
                            emitMessage(message)
                        }
                    } catch (e: Exception) {
                        Log.e("AuthViewModel", "Add tenant failed", e)
                        emitMessage("An error occurred while adding tenant.")
                    }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            ApiClient.updateAuthToken(null)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    event = AuthUiEvent.NavigateToLanding,
                    currentAdminName = null,
                    currentUserRole = null,
                    authToken = null
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

    private fun emitNavigateHome(adminName: String) {
        _uiState.update {
            it.copy(
                isLoading = false,
                currentAdminName = adminName,
                event = AuthUiEvent.NavigateHome(adminName)
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

    private fun mapGenderForApi(gender: String): String? {
        val normalized = gender.trim().lowercase()
        return when {
            normalized.isBlank() -> null
            normalized == "male" -> "male"
            normalized == "female" -> "female"
            normalized == "other" -> "other"
            normalized == "prefer not to say" -> "unspecified"
            else -> null
        }
    }

    private fun emitPropertyAdded(message: String) {
        _uiState.update {
            it.copy(
                isLoading = false,
                event = AuthUiEvent.PropertyAdded(message)
            )
        }
    }

    private fun emitTenantAdded(message: String) {
        _uiState.update {
            it.copy(
                isLoading = false,
                event = AuthUiEvent.TenantAdded(message)
            )
        }
    }
}

data class AuthUiState(
    val isLoading: Boolean = false,
    val event: AuthUiEvent? = null,
    val currentAdminName: String? = null,
    val currentUserRole: String? = null,
    val authToken: String? = null
)

sealed interface AuthUiEvent {
    data class Message(
        val message: String,
        val navigateBackToLogin: Boolean = false
    ) : AuthUiEvent

    data class NavigateHome(val adminName: String) : AuthUiEvent
    data object NavigateToLanding : AuthUiEvent
    data class PropertyAdded(val message: String) : AuthUiEvent
    data class TenantAdded(val message: String) : AuthUiEvent
}
