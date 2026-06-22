package com.example.marketlens.components.home

import com.example.marketlens.domain.models.MacroIndicator
import com.example.marketlens.domain.models.MarketNews
import com.example.marketlens.domain.repository.IMacroRepository
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
class HomeScreenViewModelTest {

    private val macroRepository: IMacroRepository = mockk(relaxed = true)
    private val newsRepository: INewsRepository = mockk(relaxed = true)

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: HomeScreenViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(android.util.Log::class)
        every { android.util.Log.d(any<String>(), any<String>()) } returns 0
        every { android.util.Log.i(any<String>(), any<String>()) } returns 0
        every { android.util.Log.w(any<String>(), any<String>()) } returns 0
        every { android.util.Log.e(any<String>(), any<String>()) } returns 0
        every { android.util.Log.e(any<String>(), any<String>(), any<Throwable>()) } returns 0

        coEvery { newsRepository.getGeneralNewsStream() } returns flowOf(emptyList())
        coEvery { macroRepository.getMacroIndicatorsStream() } returns flowOf(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic(android.util.Log::class)
    }

    @Test
    fun `initialization collects general news and split FNG from macro indicators`() = runTest {
        val mockNews = listOf(
            MarketNews(
                id = "1",
                headline = "general",
                summary = "Markets up",
                url = "https://example.com",
                source = "general",
                datetime = 123456789L,
                sentimentScore = null,
                imageUrl = "https://example.com/img.png",
                date = "2026-06-21"
            )
        )
        val mockIndicators = listOf(
            MacroIndicator("FNG", "Fear & Greed", "50", "2026-06-21"),
            MacroIndicator("GDP", "Gross Domestic Product", "3.1", "2026-03-31")
        )

        coEvery { newsRepository.getGeneralNewsStream() } returns flowOf(mockNews)
        coEvery { macroRepository.getMacroIndicatorsStream() } returns flowOf(mockIndicators)

        viewModel = HomeScreenViewModel(macroRepository, newsRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(mockNews, state.marketNews)
        assertEquals("FNG", state.fearAndGreed?.id)
        assertEquals(1, state.macroIndicators.size)
        assertEquals("GDP", state.macroIndicators[0].id)
    }

    @Test
    fun `fetchHomeData triggers refreshes on repositories`() = runTest {
        viewModel = HomeScreenViewModel(macroRepository, newsRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.fetchHomeData()
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 2) { newsRepository.refreshGeneralNews(any()) } // 1 in init, 1 in manual load
        coVerify(exactly = 2) { macroRepository.refreshFearGreedIndex() }
        coVerify(exactly = 2) { macroRepository.refreshMacroIndicators(any()) }
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(null, viewModel.uiState.value.error)
    }

    @Test
    fun `individual failure in FRED macro api is handled cleanly without blocking news`() = runTest {
        // News and FNG succeed, FRED fails
        coEvery { newsRepository.refreshGeneralNews(any()) } just Runs
        coEvery { macroRepository.refreshFearGreedIndex() } just Runs
        coEvery { macroRepository.refreshMacroIndicators(any()) } throws RuntimeException("FRED API limit exceeded")

        viewModel = HomeScreenViewModel(macroRepository, newsRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.error?.contains("Error FRED: FRED API limit exceeded") == true)
        
        // Verifies news and FNG were still called
        coVerify(exactly = 1) { newsRepository.refreshGeneralNews(any()) }
        coVerify(exactly = 1) { macroRepository.refreshFearGreedIndex() }
    }
}
