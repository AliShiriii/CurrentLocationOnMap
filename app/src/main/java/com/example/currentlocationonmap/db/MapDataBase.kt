package com.example.currentlocationonmap.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.currentlocationonmap.model.MapModel

@Database(entities = [MapModel::class], version = 1, exportSchema = false)
abstract class MapDataBase: RoomDatabase() {

    abstract fun getDao() : MapDao
}