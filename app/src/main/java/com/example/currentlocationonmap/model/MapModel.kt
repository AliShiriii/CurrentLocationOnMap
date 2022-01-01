package com.example.currentlocationonmap.model

import androidx.room.Entity

@Entity(tableName = "map")
data class MapModel(val lat: Double, val long: Double)
