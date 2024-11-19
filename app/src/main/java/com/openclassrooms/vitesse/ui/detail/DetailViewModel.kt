package com.openclassrooms.vitesse.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.vitesse.domain.model.Candidate
import com.openclassrooms.vitesse.domain.model.Favorite
import com.openclassrooms.vitesse.domain.model.PoundCurrency
import com.openclassrooms.vitesse.domain.usecase.candidate.DeleteCandidateUseCase
import com.openclassrooms.vitesse.domain.usecase.candidate.GetCandidateUseCase
import com.openclassrooms.vitesse.domain.usecase.currency.GetPoundCurrencyUseCase
import com.openclassrooms.vitesse.domain.usecase.favorite.AddFavoriteUseCase
import com.openclassrooms.vitesse.domain.usecase.favorite.DeleteFavoriteUseCase
import com.openclassrooms.vitesse.domain.usecase.favorite.GetFavoriteUseCase
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
    private val getCandidateUseCase: GetCandidateUseCase,
    private val getFavoriteUseCase: GetFavoriteUseCase,
    private val deleteCandidateUseCase: DeleteCandidateUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val getPoundCurrencyUseCase: GetPoundCurrencyUseCase
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
        _candidate.update { getCandidateUseCase.execute(id) }
    }

    fun deleteCandidate(candidate: Candidate) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteCandidateUseCase.execute(candidate)
        }
    }

    private suspend fun updateIsFavorite(candidate: Candidate) {
        isFavorite.update { getFavoriteUseCase.execute(candidate.id ?: return) != null }
    }

    fun toggleFavorite(candidate: Candidate) {
        viewModelScope.launch(Dispatchers.IO) {
            val favorite = getFavoriteUseCase.execute(candidate.id ?: return@launch)
            if (favorite != null) {
                //If the candidate is already a favorite, delete it
                deleteFavoriteUseCase.execute(candidate.id)
            } else {
                //If the candidate is not a favorite, add it
                addFavoriteUseCase.execute(Favorite(candidate.id))
            }
            updateIsFavorite(candidate)
        }
    }

    fun fetchPoundCurrency() {
        viewModelScope.launch {
            try {
                _poundCurrency.value = getPoundCurrencyUseCase.execute()
            } catch (e: Exception) {
                Log.d("DetailViewModel", "Error fetching pound currency: $e")
            }
        }
    }
}