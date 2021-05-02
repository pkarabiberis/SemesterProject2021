package com.example.kausiprojektikevat.presentation.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kausiprojektikevat.TAG
import com.example.kausiprojektikevat.data.network.model.location.Location
import com.example.kausiprojektikevat.domain.interactors.GetDistances
import com.example.kausiprojektikevat.domain.interactors.GetLocations
import com.example.kausiprojektikevat.domain.model.distance.Distance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getLocations: GetLocations,
    private val getDistances: GetDistances
) : ViewModel() {

    private val _locations = MutableLiveData<List<Location>>()
    val locations: LiveData<List<Location>> = _locations

    private val _distances = MutableLiveData<MutableList<Distance>>(mutableListOf())
    val distances: LiveData<MutableList<Distance>> = _distances

    fun getLocations() {
        viewModelScope.launch {
            _locations.postValue(getLocations.execute())
        }
    }

    fun getDistances(
        origin: String?,
        destination: String?,
        apiKey: String
    ) = viewModelScope.launch {
        val result = getDistances.execute(origin, destination, apiKey)
        result?.let {
            _distances.value?.apply {
                add(Distance(it.distance, it.address))
                _distances.postValue(this)
            }
        }
    }
}