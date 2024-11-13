package com.openclassrooms.vitesse.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
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

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class AllCandidatesViewModel @Inject constructor(private val candidateRepository: CandidateRepository) : ViewModel() {
    private val _candidatesFlow = MutableStateFlow<List<Candidate>>(emptyList())
    val candidatesFlow: StateFlow<List<Candidate>> = _candidatesFlow.asStateFlow()


    init {
        loadAllCandidates()
    }

    private fun loadAllCandidates() {
        viewModelScope.launch(Dispatchers.IO) {
            val candidates = candidateRepository.getAllCandidates()
            _candidatesFlow.value = candidates
        }
    }

    /**
     * Filter the list of candidates based on the query
     * @param query the query to filter the list of candidates
     */
    fun filter(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val candidates = candidateRepository.getAllCandidates()
            _candidatesFlow.update {
                candidates.filter { candidate ->
                    candidate.firstName.contains(query, ignoreCase = true) ||
                            candidate.lastName.contains(query, ignoreCase = true)
                }
            }
        }
    }


}