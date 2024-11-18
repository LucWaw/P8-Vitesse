package com.openclassrooms.vitesse.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.vitesse.data.api.repository.CurrencyRepository
import com.openclassrooms.vitesse.data.repository.CandidateRepository
import com.openclassrooms.vitesse.data.repository.FavoriteRepository
import com.openclassrooms.vitesse.domain.model.Candidate
import com.openclassrooms.vitesse.domain.model.Favorite
import com.openclassrooms.vitesse.domain.model.PoundCurrency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val candidateRepository: CandidateRepository,
    private val favoriteRepository: FavoriteRepository,
    private val currencyRepository: CurrencyRepository
) : ViewModel() {
    //candidate stateflow
    private val _candidate = MutableStateFlow<Candidate?>(null)
    val candidate: StateFlow<Candidate?> = _candidate.asStateFlow()
    //favorite stateflow
    private val isFavorite = MutableStateFlow(false)
    val isFavoriteFlow: StateFlow<Boolean> = isFavorite.asStateFlow()
    //currency stateflow
    private val _poundCurrency = MutableStateFlow<PoundCurrency?>(null)
    val poundCurrency: StateFlow<PoundCurrency?> = _poundCurrency


    fun loadStateFlows(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            loadCandidate(id)
            loadFavorite()
        }
    }

    private suspend fun loadFavorite() {
        _candidate.value?.let { updateIsFavorite(it) }
    }

    private suspend fun loadCandidate(id: Long) {
        _candidate.update { candidateRepository.getCandidateById(id) }
    }

    fun deleteCandidate(candidate: Candidate) {
        viewModelScope.launch(Dispatchers.IO) {
            candidateRepository.deleteCandidate(candidate)
        }
    }

    private suspend fun updateIsFavorite(candidate: Candidate) {
        isFavorite.update { favoriteRepository.getFavoriteById(candidate.id ?: return) != null }
    }

    fun toggleFavorite(candidate: Candidate) {
        viewModelScope.launch(Dispatchers.IO) {
            val favorite = favoriteRepository.getFavoriteById(candidate.id ?: return@launch)
            if (favorite != null) {
                //If the candidate is already a favorite, delete it
                favoriteRepository.deleteFavoriteById(candidate.id)
            } else {
                //If the candidate is not a favorite, add it
                favoriteRepository.addFavorite(Favorite(candidate.id))
            }
            updateIsFavorite(candidate)
        }
    }

    fun fetchPoundCurrency() {
        viewModelScope.launch {
            try {
                _poundCurrency.value = currencyRepository.getPoundCurrency()
            } catch (e: Exception) {
                Log.d("DetailViewModel", "Error fetching pound currency: $e")
            }
        }
    }
}