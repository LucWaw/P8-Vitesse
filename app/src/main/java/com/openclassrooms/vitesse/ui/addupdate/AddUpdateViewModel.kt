package com.openclassrooms.vitesse.ui.addupdate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.vitesse.domain.model.Candidate
import com.openclassrooms.vitesse.domain.usecase.candidate.AddCandidateUseCase
import com.openclassrooms.vitesse.domain.usecase.candidate.GetCandidateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddUpdateViewModel @Inject constructor(private val getCandidateUseCase: GetCandidateUseCase, private val addCandidateUseCase: AddCandidateUseCase) : ViewModel() {
    //candidate stateflow
    private val _candidate = MutableStateFlow<Candidate?>(null)
    val candidate: StateFlow<Candidate?> = _candidate.asStateFlow()

    fun loadStateFlow(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            loadCandidate(id)
        }
    }

    private suspend fun loadCandidate(id: Long) {
        _candidate.update { getCandidateUseCase.execute(id) }
    }

    fun addCandidate(candidate: Candidate) {
        viewModelScope.launch(Dispatchers.IO) {
            addCandidateUseCase.execute(candidate)
        }
    }


}