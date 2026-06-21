package com.example.marketlens.components.profile

data class ProfileScreenState(
    val userName: String = "Agustín",
    val favoriteAssets: List<String> = emptyList(),
    val isBiometricEnabled: Boolean = false
)