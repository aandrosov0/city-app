package aandrosov.city.app.ui.viewModels

import aandrosov.city.app.AppMemoryStorage
import aandrosov.city.app.AppSettings
import aandrosov.city.app.ui.states.CategoryState
import aandrosov.city.app.ui.states.NewsScreenState
import aandrosov.city.app.ui.states.NewsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Suppress("UNCHECKED_CAST")
class NewsViewModel(
    private val appSettings: AppSettings,
    private val appMemoryStorage: AppMemoryStorage,
) : ViewModel() {
    companion object {
        private const val NEWS_MEMORY_KEY = "news_viewmodel_news"
        private const val CATEGORIES_MEMORY_KEY = "news_viewmodel_categories"
        private const val CURRENT_CATEGORY_KEY = "news_viewmodel_current_category"
    }

    private val _uiState = MutableStateFlow(NewsScreenState())
    val uiState = _uiState.asStateFlow()

    private val newsCategoriesRef = Firebase.firestore.collection("news_categories")

    private var fetchingJob: Job? = null

    init {
        val isCacheLoaded = loadCache()
        if (!isCacheLoaded) {
            fetchNews()
        }

        appSettings.registerOnSettingsChangeListener(::updateFavorites)
    }

    fun fetchNews(
        category: CategoryState = CategoryState.ALL,
        searchableText: String? = null
    ) {
        fetchingJob?.cancel()
        fetchingJob = viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true, category = category)

            val cityId = appSettings.cityId
            val newsRef = Firebase.firestore.collection("cities/$cityId/news")

            var query = when (category.id) {
                CategoryState.FAVORITE.id  -> {
                    val favorites = appSettings.favoriteNews.toMutableList().apply {
                        add(-1)
                    }
                    newsRef.whereIn("id", favorites)
                }
                CategoryState.ALL.id -> newsRef
                else -> newsRef.whereEqualTo("categoryId", category.id)
            }

            val news = query
                .orderBy("publishedAt", Query.Direction.DESCENDING)
                .get()
                .await()
                .toObjects<NewsState>()

            val categories = newsCategoriesRef
                .get()
                .await()
                .toObjects<CategoryState>()

            appMemoryStorage[NEWS_MEMORY_KEY] = news
            appMemoryStorage[CATEGORIES_MEMORY_KEY] = categories
            appMemoryStorage[CURRENT_CATEGORY_KEY] = category

            _uiState.value = if (searchableText != null && searchableText.isNotBlank()) {
                _uiState.value.copy(
                    categories = categories,
                    news = news.filter { news ->
                        val regex = ".*${searchableText.lowercase()}.*".toRegex()
                        news.title.lowercase().matches(regex)
                    },
                    isLoading = false
                )
            } else {
                _uiState.value.copy(
                    categories = categories, news = news,
                    isLoading = false
                )
            }

            updateFavorites()
        }
    }

    fun toggleFavorite(newsId: Long) {
        val ids = appSettings.favoriteNews.toMutableSet()

        if (!ids.remove(newsId)) {
            ids.add(newsId)
        }

        appSettings.update(favoriteNews = ids)
    }

    override fun onCleared() {
        appSettings.unregisterOnSettingsChangeListener(::updateFavorites)
    }

    @Suppress("UNCHECKED_CAST")
    private fun loadCache(): Boolean {
        val news = appMemoryStorage[NEWS_MEMORY_KEY] as? List<NewsState>
        val categories = appMemoryStorage[CATEGORIES_MEMORY_KEY] as? List<CategoryState>
        val category = appMemoryStorage[CURRENT_CATEGORY_KEY] as? CategoryState

        val isNotEmpty = news != null && categories != null && category != null
        if (isNotEmpty) {
            _uiState.value = uiState.value.copy(
                news = news, categories = categories,
                category = category, isLoading = false
            )
        }
        return isNotEmpty
    }

    private fun updateFavorites() {
        val favoriteNews = appSettings.favoriteNews

        _uiState.value = uiState.value.copy(
            news = uiState.value.news.map {
                val isFavorite = favoriteNews.contains(it.id)
                it.copy(isFavorite = isFavorite)
            }
        )
    }
}