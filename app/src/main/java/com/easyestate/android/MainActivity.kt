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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.easyestate.android.ui.AuthUiEvent
import com.easyestate.android.ui.AuthViewModel
import com.easyestate.android.ui.HomeScreen
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
                    }
                    snackbarHostState.showSnackbar("Hi ${event.adminName}! You're in as admin.")
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
            startDestination = AppDestination.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(AppDestination.Home.route) {
                HomeScreen(
                    adminName = uiState.currentAdminName ?: "Easy Estate Admin",
                    onAddProperty = {
                        coroutineScope.launch {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            snackbarHostState.showSnackbar("Add property coming soon")
                        }
                    }
                )
            }
        }
    }
}

private enum class AppDestination(val route: String) {
    Home("home")
}

@Preview(showBackground = true)
@Composable
private fun EasyEstateAppPreview() {
    EasyEstateTheme {
        EasyEstateApp(previewViewModel = AuthViewModel())
    }
}
