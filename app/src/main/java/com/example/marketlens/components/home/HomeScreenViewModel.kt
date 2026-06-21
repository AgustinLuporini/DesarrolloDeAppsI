package com.example.marketlens.components.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketlens.data.network.NetworkConfig
import com.example.marketlens.data.network.NetworkConfig.FRED_KEY
import com.example.marketlens.domain.repository.IMacroRepository
import com.example.marketlens.domain.repository.INewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val macroRepository: IMacroRepository,
    private val newsRepository: INewsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreenState())
    val uiState: StateFlow<HomeScreenState> = _uiState.asStateFlow()

    init {
        fetchHomeData()
    }

    fun fetchHomeData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            android.util.Log.d("MarketLensTRACK", "[VIEWMODEL] !!! Nací en memoria. Saliendo a buscar datos a FRED y Finnhub ahora mismo")
            
            var newsList = emptyList<com.example.marketlens.domain.models.MarketNews>()
            var fearGreedIndex: com.example.marketlens.domain.models.MacroIndicator? = null
            var macroList = emptyList<com.example.marketlens.domain.models.MacroIndicator>()
            var errorMessage: String? = null

            // 1. Obtener noticias de Finnhub
            try {
                newsList = newsRepository.getMarketNews(NetworkConfig.FINNHUB_KEY)
            } catch (e: Exception) {
                android.util.Log.e("MarketLensTRACK", "Error al cargar noticias de Finnhub", e)
                errorMessage = "Error noticias: ${e.message}"
            }

            // 2. Obtener Índice de Miedo y Codicia
            try {
                fearGreedIndex = macroRepository.getFearGreedIndex()
            } catch (e: Exception) {
                android.util.Log.e("MarketLensTRACK", "Error al cargar Fear & Greed index", e)
                if (errorMessage == null) errorMessage = "Error Fear & Greed: ${e.message}"
            }

            // 3. Obtener Indicadores Macroeconómicos de FRED
            try {
                macroList = macroRepository.getAllMacroIndicators(NetworkConfig.FRED_KEY)
            } catch (e: Exception) {
                android.util.Log.e("MarketLensTRACK", "Error al cargar indicadores de FRED", e)
                if (errorMessage == null) errorMessage = "Error FRED: ${e.message}"
            }

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                marketNews = newsList,
                fearAndGreed = fearGreedIndex,
                macroIndicators = macroList,
                error = errorMessage
            )

            android.util.Log.d(
                "MarketLensTRACK",
                "[VIEWMODEL] Carga de Home finalizada. Estado: News size=${newsList.size}, FearGreed=${fearGreedIndex?.value}, Macro size=${macroList.size}"
            )
        }
    }
}