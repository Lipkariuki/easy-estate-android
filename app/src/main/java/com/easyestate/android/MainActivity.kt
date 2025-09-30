package com.easyestate.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.easyestate.android.ui.AuthUiEvent
import com.easyestate.android.ui.AuthViewModel
import com.easyestate.android.ui.LoginScreen
import com.easyestate.android.ui.SignUpScreen
import com.easyestate.android.ui.theme.EasyEstateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EasyEstateTheme {
                EasyEstateApp()
            }
        }
    }
}

@Composable
fun EasyEstateApp(
    previewViewModel: AuthViewModel? = null,
    authViewModel: AuthViewModel = previewViewModel ?: viewModel()
) {
    val uiState = authViewModel.uiState.collectAsStateWithLifecycle().value
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    uiState.event?.let { event ->
        LaunchedEffect(event) {
            when (event) {
                is AuthUiEvent.Message -> {
                    snackbarHostState.showSnackbar(event.message)
                    if (event.navigateBackToLogin && navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                }
            }
            authViewModel.consumeEvent()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = AuthDestination.Login.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(AuthDestination.Login.route) {
                LoginScreen(
                    onSignIn = { email, password -> authViewModel.signIn(email, password) },
                    onSignUp = { navController.navigate(AuthDestination.SignUp.route) },
                    isLoading = uiState.isLoading
                )
            }
            composable(AuthDestination.SignUp.route) {
                SignUpScreen(
                    onSignUp = { name, email, password ->
                        authViewModel.signUp(name, email, password)
                    },
                    onBackToLogin = { navController.popBackStack() },
                    isLoading = uiState.isLoading
                )
            }
        }
    }
}

private enum class AuthDestination(val route: String) {
    Login("login"),
    SignUp("signup")
}

@Preview(showBackground = true)
@Composable
private fun EasyEstateAppPreview() {
    EasyEstateTheme {
        EasyEstateApp(previewViewModel = AuthViewModel())
    }
}
