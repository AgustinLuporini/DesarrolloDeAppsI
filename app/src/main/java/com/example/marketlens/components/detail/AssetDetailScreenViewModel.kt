package com.example.marketlens.components.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketlens.data.network.NetworkConfig
import com.example.marketlens.domain.repository.IAssetRepository
import com.example.marketlens.domain.repository.INewsRepository
import com.example.marketlens.domain.repository.IAiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AssetDetailScreenViewModel @Inject constructor(
    private val newsRepository: INewsRepository,
    private val assetRepository: IAssetRepository,
    private val aiRepository: IAiRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AssetDetailScreenState())
    val uiState: StateFlow<AssetDetailScreenState> = _uiState.asStateFlow()

    private var newsCollectionJob: kotlinx.coroutines.Job? = null
    private var favoriteCollectionJob: kotlinx.coroutines.Job? = null

    fun loadAssetDetails(ticker: String, name: String, price: Double, change: Double, isCrypto: Boolean) {
        _uiState.value = _uiState.value.copy(
            ticker = ticker,
            assetName = name,
            currentPrice = price,
            changePercentage = change,
            isCrypto = isCrypto
        )

        favoriteCollectionJob?.cancel()
        favoriteCollectionJob = viewModelScope.launch {
            assetRepository.isFavoriteStream(ticker).collect { isFav ->
                _uiState.value = _uiState.value.copy(isFavorite = isFav)
            }
        }

        newsCollectionJob?.cancel()
        newsCollectionJob = viewModelScope.launch {
            newsRepository.getNewsByAssetStream(ticker).collect { newsList ->
                val stockdataData = newsList.filter { it.sentimentScore != null }
                val finnhubData = newsList.filter { it.sentimentScore == null }

                _uiState.value = _uiState.value.copy(
                    assetNews = stockdataData,
                    finnhubNews = finnhubData
                )
            }
        }

        // 1. Cargar caché de IA inmediatamente (si existe y es válida)
        viewModelScope.launch {
            val cached = aiRepository.getCachedInsight(ticker)
            val now = System.currentTimeMillis()
            if (cached != null && (now - cached.timestamp < 4 * 60 * 60 * 1000)) {
                _uiState.value = _uiState.value.copy(
                    aiSummary = cached.summaryText,
                    aiConfidenceScore = cached.confidenceScore,
                    isAiLoading = false
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    aiSummary = null,
                    aiConfidenceScore = null,
                    isAiLoading = false
                )
            }
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = _uiState.value.assetNews.isEmpty() && _uiState.value.finnhubNews.isEmpty(),
                error = null
            )
            try {
                val today = LocalDate.now().toString()
                val aWeekAgo = LocalDate.now().minusDays(7).toString()

                val sentimentJob = launch {
                    newsRepository.refreshAssetSentiment(ticker, NetworkConfig.STOCKDATA_KEY)
                }
                val corporateJob = launch {
                    newsRepository.refreshAssetNews(ticker, aWeekAgo, today, NetworkConfig.FINNHUB_KEY)
                }
                sentimentJob.join()
                corporateJob.join()

                val updatedState = if (isCrypto) {
                    _uiState.value.copy(marketCap = price, ath = price)
                } else {
                    _uiState.value.copy(openPrice = price, highPrice = price)
                }

                _uiState.value = updatedState.copy(isLoading = false)

                // 2. Verificar caché de IA tras actualizar noticias
                val cached = aiRepository.getCachedInsight(ticker)
                val now = System.currentTimeMillis()
                if (cached != null && (now - cached.timestamp < 4 * 60 * 60 * 1000)) {
                    _uiState.value = _uiState.value.copy(
                        aiSummary = cached.summaryText,
                        aiConfidenceScore = cached.confidenceScore,
                        isAiLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al unificar fuentes de noticias"
                )
            }
        }
    }

    fun generateAiInsight() {
        val ticker = _uiState.value.ticker
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isAiLoading = true)
            val latestNews = _uiState.value.assetNews + _uiState.value.finnhubNews
            val result = aiRepository.generateInsight(ticker, latestNews)
            if (result != null) {
                _uiState.value = _uiState.value.copy(
                    aiSummary = result.summaryText,
                    aiConfidenceScore = result.confidenceScore,
                    isAiLoading = false
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    aiSummary = "No se pudo generar el análisis en este momento.",
                    aiConfidenceScore = null,
                    isAiLoading = false
                )
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val currentFav = _uiState.value.isFavorite
            assetRepository.toggleFavorite(_uiState.value.ticker, !currentFav)
        }
    }
}