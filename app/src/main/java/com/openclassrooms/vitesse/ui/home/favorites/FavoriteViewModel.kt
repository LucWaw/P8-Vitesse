package com.openclassrooms.vitesse.ui.home.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.vitesse.domain.model.Candidate
import com.openclassrooms.vitesse.domain.usecase.favorite.GetAllFavoriteCandidateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val getAllFavoriteCandidateUseCase: GetAllFavoriteCandidateUseCase) : ViewModel(){
    private val _favoritesFlow = MutableStateFlow<List<Candidate>>(emptyList())
    val favoritesFlow: StateFlow<List<Candidate>> = _favoritesFlow.asStateFlow()

    init {
        loadAllFavorites()
    }

    fun loadAllFavorites(){
        viewModelScope.launch(Dispatchers.IO) {
            val favorites = getAllFavoriteCandidateUseCase.execute()
            _favoritesFlow.value = favorites
        }
    }


}