package com.openclassrooms.vitesse.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.openclassrooms.vitesse.R
import com.openclassrooms.vitesse.databinding.ActivityMainBinding
import com.openclassrooms.vitesse.domain.model.Candidate
import com.openclassrooms.vitesse.ui.addupdate.AddUpdateScreen
import com.openclassrooms.vitesse.ui.detail.DetailScreen
import com.openclassrooms.vitesse.ui.home.CandidateAdapter
import com.openclassrooms.vitesse.ui.home.ViewPagerAdapter
import com.openclassrooms.vitesse.ui.home.allcandidates.AllCandidatesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), CandidateAdapter.OnCandidateClickListener {

    private lateinit var binding: ActivityMainBinding

    private val viewModelCandidates: AllCandidatesViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setContentView(binding.root)
        setUpFab()

        binding.loading.visibility = View.VISIBLE

        setUpTabLayout()

        setUpSearch()
    }

    private fun setUpSearch() {
        //Search bar set up
        binding.searchView.setupWithSearchBar(binding.searchBar)
        binding.searchBar.navigationIcon = null

        //Add adapter to recyclerview of search
        val candidateAdapter = CandidateAdapter(this@MainActivity)
        binding.searchRecyclerview.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.searchRecyclerview.adapter = candidateAdapter

        //Hide when pressing back
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.searchView.isEnabled) {
                    binding.searchView.hide()
                }
            }
        })

        //Filter candidates according to search query
        filterCandidate(candidateAdapter)
    }


    /**
     * Filter candidates according to search query
     * @param candidateAdapter the adapter of the search recyclerview
     */
    private fun filterCandidate(candidateAdapter: CandidateAdapter) {
        binding.searchView.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim()
                if (query.isNotEmpty()) {
                    viewModelCandidates.filter(query)

                    lifecycleScope.launch {
                        viewModelCandidates.candidatesFlow.collect { candidates ->
                            candidateAdapter.submitList(candidates)
                        }
                    }

                }
            }
        })
    }

    /**
     * Set up the tab layout with the view pager
     * The view pager contains two fragments: AllItemsFragment and FavoriteFragment
     * We go on each fragment by clicking on the corresponding tab
     */
    private fun setUpTabLayout() {
        //Tab Layout set up
        val adapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.all)
                1 -> tab.text = getString(R.string.favorites)
            }
        }.attach()
    }

    /***
     * Set up the floating action button for adding a new candidate
     */
    private fun setUpFab() {
        binding.fab.setOnClickListener {
            //Go to AddUpdateScreen
            Intent(this, AddUpdateScreen::class.java).also {
                startActivity(it)
            }
        }
    }

    /**
     * When clicking on a candidate, go to the detail screen
     * @param candidate the candidate clicked
     */
    override fun onCandidateClick(candidate: Candidate) {
        val intent = Intent(this, DetailScreen::class.java).apply {
            putExtra(DetailScreen.CANDIDATE_ID, candidate.id)

        }

        startActivity(intent)
    }
}