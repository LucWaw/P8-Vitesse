package com.openclassrooms.vitesse.ui.addupdate

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.openclassrooms.vitesse.R
import com.openclassrooms.vitesse.databinding.AddCandidateBinding
import com.openclassrooms.vitesse.domain.model.Candidate
import com.openclassrooms.vitesse.ui.detail.formatAsBirthday
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class AddUpdateScreen : AppCompatActivity() {


    private lateinit var binding: AddCandidateBinding

    private val viewModel: AddUpdateViewModel by viewModels()

    private var selectedDate: String = "jj/mm/aaaa"

    private var image : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddCandidateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.addUpdateCandidate) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val candidateId = intent.getLongExtra(CANDIDATE_ID_FOR_ADD, 0L)
        setEditCandidateFields(candidateId)
        setSaveCandidate(candidateId)
        setReturnBack()
    }

    private fun setEditCandidateFields(candidateId: Long) {
        viewModel.loadStateFlow(candidateId)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.candidate.collect { candidate ->
                    if (candidate != null) {
                        binding.topAppBar.title = getString(R.string.edit_candidate)

                        binding.firstName.setText(candidate.firstName)
                        binding.lastName.setText(candidate.lastName)
                        binding.email.setText(candidate.email)
                        binding.phone.setText(candidate.phoneNumber)
                        binding.salary.setText(candidate.salaryClaim.toString())
                        binding.notes.setText(candidate.notes)
                        if (candidate.image.toString() != "") {
                            binding.image.setImageURI(candidate.image)
                            image = candidate.image
                        }

                        val birthday = candidate.birthday.formatAsBirthday(false)
                        binding.date.setText(birthday)
                    }
                }
            }
        }
    }

        private fun setReturnBack() {
            binding.topAppBar.setNavigationOnClickListener {
                finish()
            }
        }


        private fun setSaveCandidate(candidateID : Long) {
            setUpDialogBirthDate()



            // Registers a photo picker activity launcher in single-select mode.
            val pickMedia =
                registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (uri != null) {
                        image = uri
                        Log.d("PhotoPicker", "Selected URI: $uri")
                        val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
                        contentResolver.takePersistableUriPermission(uri, flag)

                    } else {
                        Log.d("PhotoPicker", "No media selected")
                    }

                    binding.image.setImageURI(uri)
                }


            binding.image.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            binding.filledButton.setOnClickListener {
                if (binding.firstName.text.toString().isEmpty()) {
                    binding.firstName.error = getString(R.string.mandatory_field)
                    return@setOnClickListener
                }
                if (binding.lastName.text.toString().isEmpty()) {
                    binding.lastName.error = getString(R.string.mandatory_field)
                    return@setOnClickListener
                }
                if (binding.email.text.toString().isEmpty()) {
                    binding.email.error = getString(R.string.mandatory_field)
                    return@setOnClickListener
                }
                if (binding.phone.text.toString().isEmpty()) {
                    binding.phone.error = getString(R.string.mandatory_field)
                    return@setOnClickListener
                }
                if (!binding.email.text.toString().isValidEmail()) {
                    binding.email.error = getString(R.string.invalid_format)
                    return@setOnClickListener
                }




                //salary, notes can be empty, set 0.0 and empty string if so
                val salary =
                    if (binding.salary.text.toString()
                            .isEmpty()
                    ) 0.0 else binding.salary.text.toString()
                        .toDouble()
                val notes =
                    binding.notes.text.toString().ifEmpty { "" }

                selectedDate = binding.date.text.toString()
                if (selectedDate == "jj/mm/aaaa") {
                    binding.date.error = getString(R.string.mandatory_field)
                    return@setOnClickListener
                }

                val candidate = Candidate(
                    id = candidateID,
                    firstName = binding.firstName.text.toString(),
                    lastName = binding.lastName.text.toString(),
                    email = binding.email.text.toString(),
                    phoneNumber = binding.phone.text.toString(),
                    birthday = LocalDate.parse(selectedDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    salaryClaim = salary,
                    notes = notes,
                    image = image ?: Uri.parse("")
                )

                viewModel.addCandidate(candidate)
                finish()
            }
        }

    private fun setUpDialogBirthDate() {
        binding.date.setOnClickListener {
            Log.d("AddUpdateScreen", "Date picker clicked")

            var date: LocalDate? = null

            if (selectedDate != "jj/mm/aaaa") {
                try {
                    date = LocalDate.parse(selectedDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                } catch (e: Exception) {
                    Log.d("AddUpdateScreen", "Invalid date format, use current date")
                }
            }

            if (date == null) {
                date = LocalDate.now()
            }

            val year = date!!.year
            val month = date.monthValue - 1  // Les mois dans le calendrier commencent à 0
            val day = date.dayOfMonth

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDateTime = LocalDate.of(
                        selectedYear, selectedMonth + 1, selectedDay
                    )
                    val formattedDate = selectedDateTime.formatAsBirthday(false)
                    binding.date.setText(formattedDate)
                    selectedDate = formattedDate
                },
                year, month, day
            )

            datePickerDialog.show()
        }
    }


    private fun CharSequence?.isValidEmail() =
            !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

        companion object {
            const val CANDIDATE_ID_FOR_ADD = "CANDIDATE_ID"
        }

    }