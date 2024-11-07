package com.example.my_mountain.dataSource

import android.util.Log
import com.example.my_mountain.model.LocationInfoModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class LocationInfoDataSource {

    private val db = Firebase.firestore

    //userUid 값으로 정보를 가져온다
    suspend fun getParkingResInfo() : List<LocationInfoModel> {
        return try {
            val querySnapshot = db.collection("location")
                .get().await()
            querySnapshot.toObjects(LocationInfoModel::class.java)
        }catch (e:Exception){
            Log.e("test1234", "${e.message}")
            emptyList()
        }
    }

}