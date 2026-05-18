package com.example.marketlens.components

sealed class Screen(val route: String){
    object Splash : Screen("splash_screen")
    object Home : Screen("home_screen")
    object Market : Screen("market_screen")
    object AssetDetail : Screen("asset_detail_screen/{assetId}") {
        fun createRoute(assetId: String) = "asset_detail_screen/$assetId"
    }
    object Profile : Screen("profile_screen")
}