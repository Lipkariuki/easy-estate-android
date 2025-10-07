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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.easyestate.android.ui.AuthUiEvent
import com.easyestate.android.ui.AddTenantScreen
import com.easyestate.android.ui.AuthViewModel
import com.easyestate.android.ui.ForgotPasswordScreen
import com.easyestate.android.ui.HomeScreen
import com.easyestate.android.ui.LandingScreen
import com.easyestate.android.ui.LoginScreen
import com.easyestate.android.ui.SendInvoiceScreen
import com.easyestate.android.ui.AddUnitScreen
import com.easyestate.android.ui.AddPropertyScreen
import com.easyestate.android.ui.PropertiesScreen
import com.easyestate.android.ui.SignUpScreen
import com.easyestate.android.ui.theme.EasyEstateTheme
import kotlinx.coroutines.launch

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
    val coroutineScope = rememberCoroutineScope()

    uiState.event?.let { event ->
        LaunchedEffect(event) {
            when (event) {
                is AuthUiEvent.Message -> {
                    snackbarHostState.showSnackbar(event.message)
                    if (event.navigateBackToLogin && navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                }
                is AuthUiEvent.NavigateHome -> {
                    navController.navigate(AppDestination.Home.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                    snackbarHostState.showSnackbar("Hi ${event.adminName}! You're in as admin.")
                }
                is AuthUiEvent.NavigateToLanding -> {
                    navController.navigate(AppDestination.Landing.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                        launchSingleTop = true
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
            startDestination = AppDestination.Landing.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(AppDestination.Landing.route) {
                LandingScreen(
                    onLoginClick = {
                        navController.navigate(AppDestination.Login.route) {
                            launchSingleTop = true
                        }
                    },
                    onSignUpClick = {
                        navController.navigate(AppDestination.SignUp.route) {
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(AppDestination.Login.route) {
                LoginScreen(
                    onSignIn = { email, password -> authViewModel.signIn(email, password) },
                    onSignUp = {
                        navController.navigate(AppDestination.SignUp.route) {
                            launchSingleTop = true
                        }
                    },
                    onForgotPassword = {
                        navController.navigate(AppDestination.ForgotPassword.route) {
                            launchSingleTop = true
                        }
                    },
                    isLoading = uiState.isLoading,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
            composable(AppDestination.SignUp.route) {
                SignUpScreen(
                    onSignUp = { firstName, lastName, email, phone, userRole, password ->
                        authViewModel.signUp(firstName, lastName, email, phone, userRole, password)
                    },
                    onBack = { navController.popBackStack() },
                    isLoading = uiState.isLoading
                )
            }
            composable(AppDestination.ForgotPassword.route) {
                ForgotPasswordScreen(
                    onBack = { navController.popBackStack() },
                    onSendReset = { email ->
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Password reset link sent to $email (stub)")
                        }
                    }
                )
            }
            composable(AppDestination.Home.route) {
                HomeScreen(
                    adminName = uiState.currentAdminName.orEmpty(),
                    onLogout = { authViewModel.logout() },
                    onAddTenantClick = { navController.navigate(AppDestination.AddTenant.route) },
                    onNavigate = { route ->
                        navController.navigate(route) {
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(AppDestination.Properties.route) {
                PropertiesScreen(
                    onBack = { navController.popBackStack() },
                    onNavigate = { route ->
                        navController.navigate(route) {
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(AppDestination.AddProperty.route) {
                AddPropertyScreen(
                    onBack = { navController.popBackStack() },
                    onAddProperty = {
                        /* TODO: Handle property submission */
                        navController.popBackStack()
                    })
            }
            composable(AppDestination.AddUnit.route) {
                AddUnitScreen(
                    onBack = { navController.popBackStack() },
                    onAddUnit = {
                        /* TODO: Handle unit submission */
                        navController.popBackStack()
                    })
            }
            composable(AppDestination.SendInvoice.route) {
                SendInvoiceScreen(
                    onBack = { navController.popBackStack() },
                    onSendInvoice = {
                        /* TODO: Handle invoice submission */
                        navController.popBackStack()
                    })
            }
        composable(AppDestination.AddTenant.route) {
            AddTenantScreen(
                onClose = { navController.popBackStack() },
                onSubmit = { _, _, _, _, _, _ ->
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Tenant submitted (stub)")
                    }
                }
            )
            }
        }
    }
}

enum class AppDestination(val route: String) {
    Landing("landing"),
    Login("login"),
    SignUp("sign_up"),
    ForgotPassword("forgot_password"),
    Home("home"),
    Properties("properties"),
    AddProperty("add_property"),
    AddUnit("add_unit"),
    SendInvoice("send_invoice"),
    AddTenant("add_tenant"),
}

@Preview(showBackground = true)
@Composable
private fun EasyEstateAppPreview() {
    EasyEstateTheme {
        EasyEstateApp(previewViewModel = AuthViewModel())
    }
}
