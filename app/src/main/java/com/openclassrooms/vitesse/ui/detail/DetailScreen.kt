package com.openclassrooms.vitesse.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.MotionEvent
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.openclassrooms.vitesse.R
import com.openclassrooms.vitesse.databinding.DetailCandidateBinding
import com.openclassrooms.vitesse.domain.model.Candidate
import com.openclassrooms.vitesse.ui.addupdate.AddUpdateScreen
import com.openclassrooms.vitesse.ui.addupdate.AddUpdateScreen.Companion.CANDIDATE_ID_FOR_ADD
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter


@AndroidEntryPoint
class DetailScreen : AppCompatActivity() {

    private lateinit var binding: DetailCandidateBinding

    private val viewModel: DetailViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = DetailCandidateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //retrieve data from put extra
        val candidateId = intent.getLongExtra(CANDIDATE_ID_FOR_DETAIL, 0)
        //viewModel.loadStateFlows(candidateId)
        viewModel.fetchPoundCurrency()

        ViewCompat.setOnApplyWindowInsetsListener(binding.detailCandidate) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.notesValue.movementMethod = ScrollingMovementMethod()

        binding.notesValue.makeScrollableInsideScrollView()

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.isFavoriteFlow.collect { isFavorite ->
                    if (isFavorite) {
                        binding.topAppBar.menu.findItem(R.id.favorite)
                            .setIcon(R.drawable.star_yellow)
                    } else {
                        binding.topAppBar.menu.findItem(R.id.favorite)
                            .setIcon(R.drawable.star_outline)
                    }
                }
            }
        }


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.loadStateFlows(candidateId)

                viewModel.candidate.collect { candidate ->
                    if (candidate != null) {
                        setCandidateData(candidate)

                        setUpDeleteButton(candidate)

                        setUpdateButton(candidate)

                        setUpFavoriteButton(candidate)

                        setUpActionButtons(candidate)
                    }
                }
            }
        }
    }

    private fun setUpActionButtons(candidate: Candidate) {
        setUpCallButton(candidate.phoneNumber)

        setUpEmailButton(candidate.email)

        setUpSmsButton(candidate.phoneNumber)

    }

    private fun setUpSmsButton(candidatePhoneNumber: String) {
        binding.smsIconButton.setOnClickListener {
            val smsUri = Uri.parse("smsto:$candidatePhoneNumber")
            val smsIntent = Intent(Intent.ACTION_VIEW, smsUri)
            startActivity(smsIntent)
        }
    }

    private fun setUpEmailButton(candidateEmail: String) {
        binding.emailIconButton.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$candidateEmail") // Uri pour envoyer un email
            }
            startActivity(emailIntent)
        }
    }

    private fun setUpCallButton(candidatePhoneNumber: String) {
        binding.callIconButton.setOnClickListener {
            val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$candidatePhoneNumber"))
            startActivity(dialIntent)
        }
    }

    private fun setUpFavoriteButton(candidate: Candidate) {
        binding.topAppBar.menu.findItem(R.id.favorite).setOnMenuItemClickListener {
            viewModel.toggleFavorite(candidate)
            binding.topAppBar.menu.findItem(R.id.favorite).setIcon(R.drawable.star_yellow)


            true
        }
    }

    private fun setUpdateButton(candidate: Candidate) {
        val intent = Intent(this, AddUpdateScreen::class.java).apply {
            putExtra(CANDIDATE_ID_FOR_ADD, candidate.id)
        }
        binding.topAppBar.menu.findItem(R.id.edit).setOnMenuItemClickListener {
            startActivity(intent)
            true
        }
    }

    private fun setUpDeleteButton(candidate: Candidate) {
        binding.topAppBar.menu.findItem(R.id.delete).setOnMenuItemClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(resources.getString(R.string.deleteDialogTitle))
                .setMessage(resources.getString(R.string.deleteDialogMessage))
                .setNegativeButton(resources.getString(R.string.declineDeleteDialog)) { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton(resources.getString(R.string.acceptDeleteDialog)) { _, _ ->
                    viewModel.deleteCandidate(candidate)
                    finish()
                }
                .show()
            true
        }
    }


    private fun TextView.makeScrollableInsideScrollView() {
        movementMethod = ScrollingMovementMethod()
        setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_UP -> {
                    v.parent.requestDisallowInterceptTouchEvent(false)
                    //It is required to call performClick() in onTouch event.
                    performClick()
                }
            }
            false
        }
    }

    private fun setCandidateData(candidate: Candidate) {

        if (candidate.image.toString() != "") {
            binding.image.setImageURI(candidate.image)
        }

        binding.topAppBar.title = candidate.firstName + " " + candidate.lastName.uppercase()
        binding.aboutAge.text = candidate.birthday.formatAsBirthday(true)
        @SuppressLint("SetTextI18n")// No need translation
        binding.eurosSalary.text = candidate.salaryClaim.toString() + " €"

        updatePoundValue(candidate)

        binding.notesValue.text = candidate.notes

    }

    @SuppressLint("SetTextI18n")// No need translation
    private fun updatePoundValue(candidate: Candidate) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.poundCurrency.collect { poundCurrency ->
                    if (poundCurrency != null) {
                        val poundValue = candidate.salaryClaim * poundCurrency.gbpValue
                        val roundedValue = BigDecimal(poundValue).setScale(2, RoundingMode.HALF_UP).toDouble()

                        binding.poundSalary.text = "£ $roundedValue"
                    }
                }
            }
        }
    }

    companion object {
        const val CANDIDATE_ID_FOR_DETAIL = "CANDIDATE_ID"
    }
}

fun LocalDate.formatAsBirthday(addAge : Boolean): String {
    // Formater la date
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val formattedDate = this.format(dateFormatter)

    // Calculer l'âge
    return if (addAge){
        val currentDate = LocalDate.now()
        val age = Period.between(this, currentDate).years

        // Construire le résultat final
        "$formattedDate ($age ans)"
    }else formattedDate

}


