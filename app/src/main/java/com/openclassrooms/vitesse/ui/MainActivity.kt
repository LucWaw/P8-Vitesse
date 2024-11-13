package com.openclassrooms.vitesse.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.openclassrooms.vitesse.R
import com.openclassrooms.vitesse.databinding.ActivityMainBinding
import com.openclassrooms.vitesse.ui.addupdate.AddUpdateScreen
import com.openclassrooms.vitesse.ui.home.AllCandidatesViewModel
import com.openclassrooms.vitesse.ui.home.CandidateAdapter
import com.openclassrooms.vitesse.ui.home.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModelCandidates: AllCandidatesViewModel by viewModels()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpFab()


        binding.loading.visibility = View.VISIBLE

        //Tab Layout set up
        val adapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "All"
                1 -> tab.text = "Favorites"
            }
        }.attach()

        //Search bar set up
        binding.searchView.setupWithSearchBar(binding.searchBar)
        binding.searchBar.navigationIcon = null
        val candidateAdapter = CandidateAdapter()
        binding.searchRecyclerview.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.searchRecyclerview.adapter = candidateAdapter
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

    private fun setUpFab() {
        binding.fab.setOnClickListener {
            //Go to AddUpdateScreen
            Intent(this, AddUpdateScreen::class.java).also {
                startActivity(it)
            }
        }
    }
}