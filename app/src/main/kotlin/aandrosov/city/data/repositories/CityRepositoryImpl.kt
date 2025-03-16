package aandrosov.city.data.repositories

import aandrosov.city.data.models.CityModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CityRepositoryImpl(
    private val firestore: FirebaseFirestore = Firebase.firestore,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : CityRepository {
    override suspend fun getAll(): List<CityModel> = withContext(dispatcher) {
        firestore
            .collection("cities")
            .get()
            .await()
            .toObjects(CityModel::class.java)
    }
}