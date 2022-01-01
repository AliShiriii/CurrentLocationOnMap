package com.example.currentlocationonmap.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.currentlocationonmap.model.MapModel

@Dao
interface MapDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLatLon(mapModel: MapModel)

}