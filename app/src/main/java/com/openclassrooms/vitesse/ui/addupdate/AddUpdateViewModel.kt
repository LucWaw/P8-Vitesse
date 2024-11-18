package com.openclassrooms.vitesse.ui.addupdate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.vitesse.data.repository.CandidateRepository
import com.openclassrooms.vitesse.domain.model.Candidate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddUpdateViewModel @Inject constructor(private val candidateRepository: CandidateRepository) : ViewModel() {
    //candidate stateflow
    private val _candidate = MutableStateFlow<Candidate?>(null)
    val candidate: StateFlow<Candidate?> = _candidate.asStateFlow()

    fun loadStateFlow(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            loadCandidate(id)
        }
    }

    private suspend fun loadCandidate(id: Long) {
        _candidate.update { candidateRepository.getCandidateById(id) }
    }

    fun addCandidate(candidate: Candidate) {
        viewModelScope.launch(Dispatchers.IO) {
            candidateRepository.addCandidate(candidate)
        }
    }


}