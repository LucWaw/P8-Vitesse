package com.openclassrooms.vitesse.ui.home.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.vitesse.data.repository.FavoriteRepository
import com.openclassrooms.vitesse.domain.model.Candidate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val favoriteRepository: FavoriteRepository) : ViewModel(){
    private val _favoritesFlow = MutableStateFlow<List<Candidate>>(emptyList())
    val favoritesFlow: StateFlow<List<Candidate>> = _favoritesFlow.asStateFlow()

    init {
        loadAllFavorites()
    }

    fun loadAllFavorites(){
        viewModelScope.launch(Dispatchers.IO) {
            val favorites = favoriteRepository.getAllFavoritesCandidates()
            _favoritesFlow.value = favorites
        }
    }


}