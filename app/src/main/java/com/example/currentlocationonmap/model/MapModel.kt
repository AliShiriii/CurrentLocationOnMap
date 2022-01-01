package com.example.currentlocationonmap.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mapModel")
data class MapModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val lat: Double?,
    val lon: Double?)