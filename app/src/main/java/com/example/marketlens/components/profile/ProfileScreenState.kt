package com.example.marketlens.components.profile

data class ProfileScreenState(
    val userName: String = "Agustín",
    val favoriteAssets: List<String> = listOf("BTC", "AAPL", "ETH", "TSLA", "MSFT")
)