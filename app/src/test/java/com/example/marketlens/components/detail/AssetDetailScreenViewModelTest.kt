package com.example.marketlens.components.detail

import com.example.marketlens.domain.models.AiInsight
import com.example.marketlens.domain.models.MarketNews
import com.example.marketlens.domain.repository.IAiRepository
import com.example.marketlens.domain.repository.IAssetRepository
import com.example.marketlens.domain.repository.INewsRepository
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
class AssetDetailScreenViewModelTest {

    private val newsRepository: INewsRepository = mockk(relaxed = true)
    private val assetRepository: IAssetRepository = mockk(relaxed = true)
    private val aiRepository: IAiRepository = mockk(relaxed = true)

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: AssetDetailScreenViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        
        coEvery { assetRepository.isFavoriteStream(any()) } returns flowOf(false)
        coEvery { newsRepository.getNewsByAssetStream(any()) } returns flowOf(emptyList())

        viewModel = AssetDetailScreenViewModel(
            newsRepository = newsRepository,
            assetRepository = assetRepository,
            aiRepository = aiRepository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when cache is valid (less than 4 hours), it is loaded immediately and generateInsight is NOT called`() = runTest {
        val ticker = "AAPL"
        val validTimestamp = System.currentTimeMillis() - (1 * 60 * 60 * 1000) // 1 hour ago
        val cachedInsight = AiInsight(ticker, "Cached AI Summary", 85, validTimestamp)

        coEvery { aiRepository.getCachedInsight(ticker) } returns cachedInsight

        viewModel.loadAssetDetails(ticker, "Apple Inc.", 150.0, 1.5, false)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("Cached AI Summary", state.aiSummary)
        assertEquals(85, state.aiConfidenceScore)
        assertFalse(state.isAiLoading)

        coVerify(exactly = 0) { aiRepository.generateInsight(any(), any()) }
    }

    @Test
    fun `when cache is expired (more than 4 hours), generateAiInsight triggers generateInsight`() = runTest {
        val ticker = "AAPL"
        val expiredTimestamp = System.currentTimeMillis() - (5 * 60 * 60 * 1000) // 5 hours ago
        val expiredInsight = AiInsight(ticker, "Expired AI Summary", 80, expiredTimestamp)
        val freshInsight = AiInsight(ticker, "Fresh AI Summary", 90, System.currentTimeMillis())

        coEvery { aiRepository.getCachedInsight(ticker) } returns expiredInsight
        coEvery { aiRepository.generateInsight(ticker, any()) } returns freshInsight

        viewModel.loadAssetDetails(ticker, "Apple Inc.", 150.0, 1.5, false)
        testDispatcher.scheduler.advanceUntilIdle()

        val initialState = viewModel.uiState.value
        assertEquals(null, initialState.aiSummary)
        assertEquals(null, initialState.aiConfidenceScore)

        viewModel.generateAiInsight()
        testDispatcher.scheduler.advanceUntilIdle()

        val finalState = viewModel.uiState.value
        assertEquals("Fresh AI Summary", finalState.aiSummary)
        assertEquals(90, finalState.aiConfidenceScore)
        assertFalse(finalState.isAiLoading)

        coVerify(exactly = 1) { aiRepository.generateInsight(ticker, any()) }
    }

    @Test
    fun `when cache is missing, generateAiInsight triggers generateInsight`() = runTest {
        val ticker = "AAPL"
        val freshInsight = AiInsight(ticker, "Fresh AI Summary", 95, System.currentTimeMillis())

        coEvery { aiRepository.getCachedInsight(ticker) } returns null
        coEvery { aiRepository.generateInsight(ticker, any()) } returns freshInsight

        viewModel.loadAssetDetails(ticker, "Apple Inc.", 150.0, 1.5, false)
        testDispatcher.scheduler.advanceUntilIdle()

        val initialState = viewModel.uiState.value
        assertEquals(null, initialState.aiSummary)

        viewModel.generateAiInsight()
        testDispatcher.scheduler.advanceUntilIdle()

        val finalState = viewModel.uiState.value
        assertEquals("Fresh AI Summary", finalState.aiSummary)
        assertEquals(95, finalState.aiConfidenceScore)
        assertFalse(finalState.isAiLoading)

        coVerify(exactly = 1) { aiRepository.generateInsight(ticker, any()) }
    }
}
