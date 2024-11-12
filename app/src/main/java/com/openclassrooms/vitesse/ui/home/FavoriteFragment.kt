package com.openclassrooms.vitesse.ui.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.vitesse.databinding.RecyclerCandidatesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment  : Fragment() {
    private var _binding: RecyclerCandidatesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteViewModel by viewModels()
    private lateinit var candidateAdapter: CandidateAdapter

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RecyclerCandidatesBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeCandidates()
        //setUpFab()
    }

    private fun setUpFab() {
        TODO("Not yet implemented")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeCandidates() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.favoritesFlow.collect { candidates ->
                candidateAdapter.submitList(candidates)
            }
        }
    }

    private fun setupRecyclerView() {
        candidateAdapter = CandidateAdapter()
        binding.candidateRecyclerview.layoutManager = LinearLayoutManager(context)
        binding.candidateRecyclerview.adapter = candidateAdapter
    }
}
