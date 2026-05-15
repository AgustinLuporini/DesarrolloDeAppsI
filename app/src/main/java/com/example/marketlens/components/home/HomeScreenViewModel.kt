package com.example.marketlens.components.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketlens.data.network.NetworkConfig
import com.example.marketlens.data.network.NetworkConfig.FRED_KEY
import com.example.marketlens.data.repository.MacroRepository
import com.example.marketlens.data.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel : ViewModel() {
    private val macroRepository = MacroRepository()
    private val newsRepository = NewsRepository()

    private val _uiState = MutableStateFlow(HomeScreenState())
    val uiState: StateFlow<HomeScreenState> = _uiState.asStateFlow()

    init {
        fetchHomeData()
    }

    fun fetchHomeData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val news = newsRepository.getMarketNews(NetworkConfig.FINNHUB_KEY)
                val fearGreed = macroRepository.getFearGreedIndex()
                val macro = macroRepository.getAllMacroIndicators(NetworkConfig.FRED_KEY)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    marketNews = news,
                    fearAndGreed = fearGreed,
                    macroIndicators = macro
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }
}