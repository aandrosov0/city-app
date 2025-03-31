package aandrosov.city.app.ui.viewModels

import aandrosov.city.app.ui.states.CategoryState
import aandrosov.city.app.ui.states.NewsScreenState
import aandrosov.city.app.ui.states.NewsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class NewsViewModel(
    private val appViewModel: AppViewModel,
    private val firestore: FirebaseFirestore = Firebase.firestore,
) : ViewModel() {
    private val _uiState = MutableStateFlow(NewsScreenState())
    val uiState = _uiState.asStateFlow()

    private val newsCategoriesRef = firestore.collection("news_categories")

    private var fetchingJob: Job? = null

    private var cityId: Long = 0
    init {
        viewModelScope.launch {
            appViewModel.uiState.collect {
                cityId = it.settings.cityId
            }
        }
    }

    fun fetchNews(categoryId: Long? = null) {
        fetchingJob?.cancel()
        fetchingJob = viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val newsRef = firestore.collection("cities/$cityId/news")

            val query = categoryId?.let { newsRef.whereEqualTo("categoryId", it) } ?: newsRef

            val news = query
                .orderBy("publishedAt", Query.Direction.DESCENDING)
                .get()
                .await()
                .toObjects<NewsState>()

            val categories = newsCategoriesRef
                .get()
                .await()
                .toObjects<CategoryState>()

            _uiState.value = _uiState.value.copy(categories = categories, news = news, isLoading = false)
        }
    }
}