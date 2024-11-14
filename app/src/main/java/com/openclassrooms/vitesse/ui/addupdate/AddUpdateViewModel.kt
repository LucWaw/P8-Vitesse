package com.openclassrooms.vitesse.ui.addupdate

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.vitesse.data.repository.CandidateRepository
import com.openclassrooms.vitesse.domain.model.Candidate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddUpdateViewModel @Inject constructor(private val candidateRepository: CandidateRepository) : ViewModel() {
    @RequiresApi(Build.VERSION_CODES.O)
    fun addCandidate(Candidate: Candidate) {
        viewModelScope.launch(Dispatchers.IO) {
            candidateRepository.addCandidate(Candidate)
        }
    }
}