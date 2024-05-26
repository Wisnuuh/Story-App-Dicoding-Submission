package com.dicoding.storyapp.view.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.UserRepository
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: UserRepository): ViewModel() {

    fun getLocations() = repository.getLocations()
}