package com.openclassrooms.vitesse.ui.home.allcandidates

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.vitesse.databinding.RecyclerCandidatesBinding
import com.openclassrooms.vitesse.domain.model.Candidate
import com.openclassrooms.vitesse.ui.detail.DetailScreen
import com.openclassrooms.vitesse.ui.home.CandidateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllItemsFragment : Fragment(), CandidateAdapter.OnCandidateClickListener {
    private var _binding: RecyclerCandidatesBinding? = null
    private val binding get() = _binding!!



    private val viewModel: AllCandidatesViewModel by viewModels()
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


    override fun onResume() {
        super.onResume()
        viewModel.loadAllCandidates()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.candidatesFlow.collect { candidates ->
                if (candidates.isEmpty()) {
                    binding.noData.visibility = View.VISIBLE
                    binding.loading.visibility = View.GONE
                } else {
                    binding.noData.visibility = View.GONE
                    binding.loading.visibility = View.GONE
                }
            }
        }
        context?.let { super.onAttach(it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loading.visibility = View.VISIBLE
        setupRecyclerView()
        observeCandidates()


    }


    private fun observeCandidates() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.candidatesFlow.collect { candidates ->
                candidateAdapter.submitList(candidates)
                if (candidates.isEmpty()) {
                    binding.noData.visibility = View.VISIBLE
                    binding.loading.visibility = View.GONE
                } else {
                    binding.noData.visibility = View.GONE
                    binding.loading.visibility = View.GONE
                }
            }
        }
    }

    private fun setupRecyclerView() {
        candidateAdapter = CandidateAdapter(this)
        binding.candidateRecyclerview.layoutManager = LinearLayoutManager(context)
        binding.candidateRecyclerview.adapter = candidateAdapter
    }

    override fun onCandidateClick(candidate: Candidate) {
        val intent = Intent(this.context, DetailScreen::class.java).apply {
            putExtra(DetailScreen.CANDIDATE_ID_FOR_DETAIL, candidate.id)

        }

        startActivity(intent)
    }

}
