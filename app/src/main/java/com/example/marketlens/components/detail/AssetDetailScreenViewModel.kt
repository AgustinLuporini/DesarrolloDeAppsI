package com.example.marketlens.components.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketlens.data.network.NetworkConfig
import com.example.marketlens.domain.repository.INewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AssetDetailScreenViewModel @Inject constructor(
    private val newsRepository: INewsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AssetDetailScreenState())
    val uiState: StateFlow<AssetDetailScreenState> = _uiState.asStateFlow()

    fun loadAssetDetails(ticker: String, name: String, price: Double, change: Double, isCrypto: Boolean) {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            ticker = ticker,
            assetName = name,
            currentPrice = price,
            changePercentage = change,
            isCrypto = isCrypto
        )

        viewModelScope.launch {
            try {
                val today = LocalDate.now().toString()
                val aWeekAgo = LocalDate.now().minusDays(7).toString()

                val stockdataData = newsRepository.getAssetSentiment(ticker, NetworkConfig.STOCKDATA_KEY)
                val finnhubData = newsRepository.getAssetNews(ticker, aWeekAgo, today, NetworkConfig.FINNHUB_KEY)

                val mockAiSummary = "Analizando el sentimiento de las ${stockdataData.size + finnhubData.size} noticias encontradas para $ticker... Próximamente resumen con IA."

                val updatedState = if (isCrypto) {
                    _uiState.value.copy(marketCap = price, ath = price)
                } else {
                    _uiState.value.copy(openPrice = price, highPrice = price)
                }

                _uiState.value = updatedState.copy(
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