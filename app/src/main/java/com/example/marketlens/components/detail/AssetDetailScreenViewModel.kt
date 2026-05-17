package com.example.marketlens.components.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketlens.data.network.NetworkConfig
import com.example.marketlens.data.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class AssetDetailScreenViewModel : ViewModel() {
    private val newsRepository = NewsRepository()

    private val _uiState = MutableStateFlow(AssetDetailScreenState())
    val uiState: StateFlow<AssetDetailScreenState> = _uiState.asStateFlow()

    fun loadAssetDetails(ticker: String, name: String, price: Double, change: Double) {
        // Inicializamos el estado con los datos que ya tenemos de la lista
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            ticker = ticker,
            assetName = name,
            currentPrice = price,
            changePercentage = change
        )

        viewModelScope.launch {
            try {
                // Calculo de fechas
                val today = LocalDate.now().toString()
                val aWeekAgo = LocalDate.now().minusDays(7).toString()

                // Llamado a Stockdata
                val stockdataData = newsRepository.getAssetSentiment(ticker, NetworkConfig.STOCKDATA_KEY)

                // 2. Llamada a Finnhub
                val finnhubData = newsRepository.getAssetNews(ticker, aWeekAgo, today, NetworkConfig.FINNHUB_KEY)

                val mockAiSummary = "Analizando el sentimiento de las ${stockdataData.size + finnhubData.size} noticias encontradas para $ticker... Próximamente resumen con IA."

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    assetNews = stockdataData,
                    finnhubNews = finnhubData,
                    aiSummary = mockAiSummary
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al unificar fuentes de noticias"
                )
            }
        }
    }
}