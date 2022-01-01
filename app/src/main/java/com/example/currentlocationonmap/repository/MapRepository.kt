package com.example.currentlocationonmap.repository

import com.example.currentlocationonmap.db.MapDao
import com.example.currentlocationonmap.model.MapModel
import javax.inject.Inject

class MapRepository @Inject constructor(private val mapDao: MapDao) {

    suspend fun insertLatLon(mapModel: MapModel) = mapDao.insertLatLon(mapModel)

}