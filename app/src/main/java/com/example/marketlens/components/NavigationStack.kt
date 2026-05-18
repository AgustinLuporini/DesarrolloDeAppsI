package com.example.marketlens.components

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.marketlens.components.detail.AssetDetailScreen
import com.example.marketlens.components.home.HomeScreen
import com.example.marketlens.components.home.HomeScreenViewModel
import com.example.marketlens.components.market.MarketScreen
import com.example.marketlens.components.splash.SplashScreen

@Composable
fun NavigationStack() {
    val navController = rememberNavController()
    val sharedHomeViewModel: HomeScreenViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // Splash
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        // Pantalla Principal
        composable(Screen.Home.route) {
            HomeScreen(navController = navController, viewModel = sharedHomeViewModel)
        }

        // Pantalla de Mercados
        composable(Screen.Market.route) {
            MarketScreen(navController)
        }

        // Perfil
        composable(Screen.Profile.route) {
            // ProfileScreen(navController)
        }

        // Detail
        composable(
            route = "asset_detail_screen/{ticker}/{name}/{price}/{change}"
        ) { backStackEntry ->
            val ticker = backStackEntry.arguments?.getString("ticker") ?: ""
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val price = backStackEntry.arguments?.getString("price")?.toDoubleOrNull() ?: 0.0
            val change = backStackEntry.arguments?.getString("change")?.toDoubleOrNull() ?: 0.0

            AssetDetailScreen(ticker = ticker, name = name, price = price, change = change)
        }
    }
}