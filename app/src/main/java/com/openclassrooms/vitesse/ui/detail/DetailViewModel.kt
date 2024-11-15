package com.openclassrooms.vitesse.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.vitesse.data.repository.CandidateRepository
import com.openclassrooms.vitesse.data.repository.FavoriteRepository
import com.openclassrooms.vitesse.domain.model.Candidate
import com.openclassrooms.vitesse.domain.model.Favorite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val candidateRepository: CandidateRepository, private val favoriteRepository: FavoriteRepository) : ViewModel()
{
    //candidate stateflow
    private val _candidate = MutableStateFlow<Candidate?>(null)
    val candidate : StateFlow<Candidate?> = _candidate.asStateFlow()

    private val isFavorite = MutableStateFlow(false)
    val isFavoriteFlow : StateFlow<Boolean> = isFavorite.asStateFlow()




    fun loadCandidate(id : Long) {
        viewModelScope.launch(Dispatchers.IO) {
            //update
            _candidate.update {

                candidateRepository.getCandidateById(id) }.run {
                changeUpdateFavorite(_candidate.value?: return@launch)

            }
            //check if candidate is favorite

        }

    }

    fun deleteCandidate(candidate: Candidate) {
        viewModelScope.launch(Dispatchers.IO) {
            candidateRepository.deleteCandidate(candidate)
        }
    }

    private suspend fun changeUpdateFavorite(candidate: Candidate) {
            val favorite = favoriteRepository.getFavoriteById(candidate.id?: return)
            if (favorite != null) {
                isFavorite.update {
                    true
                }
            } else {
                isFavorite.update {
                    false
                }
            }
    }

    fun toggleFavorite(candidate: Candidate) {
        //check if candidate is already favorite
        viewModelScope.launch(Dispatchers.IO) {
            val favorite = favoriteRepository.getFavoriteById(candidate.id?: return@launch)
            if (favorite != null) {
                favoriteRepository.deleteFavoriteById(candidate.id)
                isFavorite.update {
                    false
                }
            } else {
                viewModelScope.launch(Dispatchers.IO) {
                    favoriteRepository.addFavorite(Favorite(candidate.id))
                }
                isFavorite.update {
                    true
                }
            }
        }


    }
}