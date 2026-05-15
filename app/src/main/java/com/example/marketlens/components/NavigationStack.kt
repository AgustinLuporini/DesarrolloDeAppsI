package com.example.marketlens.components

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.marketlens.components.home.HomeScreen

@Composable
fun NavigationStack() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // Pantalla Principal
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        // Pantalla de Mercados (La crearemos después)
        composable(Screen.Market.route) {
            // MarketScreen(navController)
        }

        // Perfil
        composable(Screen.Profile.route) {
            // ProfileScreen(navController)
        }
    }
}