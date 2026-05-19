package com.example.marketlens.components.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.marketlens.components.Screen

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    vm: LoginScreenViewModel = viewModel(),
    onGoogleLoginClick: () -> Unit
) {

    LaunchedEffect(Unit) {
        vm.uiEvent.collect { event ->
            if (event == "loginOK") {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Splash.route) {
                        inclusive = true
                    }
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onGoogleLoginClick,
            shape = RoundedCornerShape(24.dp),
            elevation = ButtonDefaults.buttonElevation(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Text("Iniciar sesión con Google", color = Color.Black)
        }
    }
}