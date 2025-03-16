package aandrosov.city.data.repositories

import aandrosov.city.data.models.NewsModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class NewsRepositoryImpl(
    private val firestore: FirebaseFirestore = Firebase.firestore,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : NewsRepository {
    override suspend fun getByCityId(cityId: Long): List<NewsModel> = withContext(dispatcher) {
        firestore
            .collection("news")
            .whereEqualTo("cityId", cityId)
            .get()
            .await()
            .toObjects(NewsModel::class.java)
    }
}