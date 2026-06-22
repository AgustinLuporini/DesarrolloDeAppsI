package com.example.marketlens.data.repository

import com.example.marketlens.data.datasource.AssetApiDataSource
import com.example.marketlens.data.local.AssetDao
import com.example.marketlens.data.local.AssetEntity
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AssetRepositoryTest {

    private val dataSource: AssetApiDataSource = mockk(relaxed = true)
    private val assetDao: AssetDao = mockk(relaxed = true)
    private val firestore: FirebaseFirestore = mockk(relaxed = true)
    private val firebaseAuth: FirebaseAuth = mockk(relaxed = true)

    private val firebaseUser: FirebaseUser = mockk(relaxed = true)
    private val usersCollection: CollectionReference = mockk(relaxed = true)
    private val watchlistCollection: CollectionReference = mockk(relaxed = true)
    private val userDocRef: DocumentReference = mockk(relaxed = true)
    private val watchlistDocRef: DocumentReference = mockk(relaxed = true)

    private lateinit var repository: AssetRepository

    @Before
    fun setUp() {
        every { firebaseAuth.currentUser } returns firebaseUser
        every { firebaseUser.uid } returns "test_user_id"
        every { firebaseUser.email } returns "test@example.com"
        every { firebaseUser.displayName } returns "Test User"
        every { firebaseUser.photoUrl } returns null

        every { firestore.collection("users") } returns usersCollection
        every { usersCollection.document("test_user_id") } returns userDocRef
        every { userDocRef.set(any<Map<String, Any>>()) } returns Tasks.forResult(null)

        every { firestore.collection("user_watchlist") } returns watchlistCollection
        every { watchlistCollection.document("test_user_id") } returns watchlistDocRef
        every { watchlistDocRef.set(any<Map<String, Any>>()) } returns Tasks.forResult(null)
        every { watchlistDocRef.update(any<String>(), any<Any>()) } returns Tasks.forResult(null)

        repository = AssetRepository(
            dataSource = dataSource,
            assetDao = assetDao,
            firestore = firestore,
            firebaseAuth = firebaseAuth
        )
    }

    @Test
    fun `toggleFavorite updates Room locally and updates Firestore watchlists`() = runTest {
        repository.toggleFavorite("BTC", true)

        coVerify(exactly = 1) { assetDao.updateFavoriteStatus("BTC", true) }
        verify(exactly = 1) { watchlistDocRef.update("favorites", any()) }
    }

    @Test
    fun `syncUserSession merges local and remote favorites, saving back to both Room and Firestore`() = runTest {
        val mockSnapshot = mockk<DocumentSnapshot>()
        every { mockSnapshot.get("favorites") } returns listOf("ETH", "ADA")
        every { mockSnapshot.exists() } returns true
        every { watchlistDocRef.get() } returns Tasks.forResult(mockSnapshot)

        coEvery { assetDao.getFavoritesIds() } returns listOf("BTC", "ETH")

        repository.syncUserSession(firebaseUser)

        coVerify(exactly = 1) { assetDao.updateFavoriteStatus("BTC", true) }
        coVerify(exactly = 1) { assetDao.updateFavoriteStatus("ETH", true) }
        coVerify(exactly = 1) { assetDao.updateFavoriteStatus("ADA", true) }

        coVerify(exactly = 1) { assetDao.insertAssets(match { list -> list.any { it.id == "ADA" } }) }

        verify(exactly = 1) { watchlistDocRef.set(mapOf("favorites" to listOf("BTC", "ETH", "ADA"))) }
    }
}
