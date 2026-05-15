package com.example.marketlens.components.home

import com.example.marketlens.domain.models.MarketNews
import com.example.marketlens.domain.models.MacroIndicator

data class HomeScreenState(
    val isLoading: Boolean = false,
    val marketNews: List<MarketNews> = emptyList(),
    val fearAndGreed: MacroIndicator? = null,
    val macroIndicators: List<MacroIndicator> = emptyList(),
    val error: String? = null
)