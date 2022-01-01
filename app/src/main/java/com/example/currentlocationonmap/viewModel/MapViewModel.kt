package com.example.currentlocationonmap.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currentlocationonmap.model.MapModel
import com.example.currentlocationonmap.repository.MapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(private val repository: MapRepository) : ViewModel() {

    fun insertLatLon(mapModel: MapModel) = viewModelScope.launch {

            repository.insertLatLon(mapModel)

        }
}