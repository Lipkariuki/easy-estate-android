package com.easyestate.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.easyestate.android.ui.theme.EasyEstateTheme
import com.easyestate.android.ui.LoginScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EasyEstateTheme {
                LoginScreen(
                    onSignIn = { _, _ ->
                        // TODO: wire to authentication flow
                    },
                    onSignUp = {
                        // TODO: navigate to sign-up once implemented
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    EasyEstateTheme {
        LoginScreen(onSignIn = { _, _ -> }, onSignUp = {})
    }
}
