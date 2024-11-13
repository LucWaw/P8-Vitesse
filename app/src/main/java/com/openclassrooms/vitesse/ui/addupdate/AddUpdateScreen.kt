package com.openclassrooms.vitesse.ui.addupdate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.openclassrooms.vitesse.databinding.AddCandidateBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddUpdateScreen : AppCompatActivity() {

    private lateinit var binding: AddCandidateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddCandidateBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}