package com.example.marketlens.domain.models

data class MacroIndicator(
    val value: String,
    val description: String,
    val date: String? = null
)