package com.example.marketlens.components.market

import com.example.marketlens.domain.models.Crypto
import com.example.marketlens.domain.models.Stock
import com.example.marketlens.domain.repository.IAssetRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MarketScreenViewModelTest {

    private val assetRepository: IAssetRepository = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: MarketScreenViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        // Mock stream values to avoid hanging
        coEvery { assetRepository.getCryptosStream() } returns flowOf(emptyList())
        coEvery { assetRepository.getStocksStream() } returns flowOf(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initialization collects cryptos and stocks from repository`() = runTest {
        val mockCryptos = listOf(
            Crypto("BTC", "Bitcoin", 60000.0, 2.5, null, 1200000.0, 69000.0)
        )
        val mockStocks = listOf(
            Stock(
                ticker = "AAPL",
                name = "Apple Inc.",
                currentPrice = 150.0,
                changePercentage = 1.2,
                imageUrl = null,
                openPrice = 149.0,
                highPrice = 151.0
            )
        )

        coEvery { assetRepository.getCryptosStream() } returns flowOf(mockCryptos)
        coEvery { assetRepository.getStocksStream() } returns flowOf(mockStocks)

        viewModel = MarketScreenViewModel(assetRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(mockCryptos, state.cryptos)
        assertEquals(mockStocks, state.stocks)
    }

    @Test
    fun `loadMarketData triggers network refresh on repository`() = runTest {
        viewModel = MarketScreenViewModel(assetRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.loadMarketData()
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 2) { assetRepository.refreshCryptos() } // 1 in init, 1 in manual load
        coVerify(exactly = 2) { assetRepository.refreshStocks(any(), any()) } // 1 in init, 1 in manual load
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(null, viewModel.uiState.value.error)
    }

    @Test
    fun `when refresh fails, error message is populated in uiState`() = runTest {
        coEvery { assetRepository.refreshCryptos() } throws RuntimeException("CoinGecko API Limit")

        viewModel = MarketScreenViewModel(assetRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("CoinGecko API Limit", state.error)
    }

    @Test
    fun `search query updates search text in state`() = runTest {
        viewModel = MarketScreenViewModel(assetRepository)
        viewModel.onSearchQueryChanged("bitcoin")

        assertEquals("bitcoin", viewModel.uiState.value.searchQuery)
    }
}
