package com.openclassrooms.vitesse.ui.addupdate

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
import com.openclassrooms.vitesse.R
import com.openclassrooms.vitesse.databinding.AddCandidateBinding
import com.openclassrooms.vitesse.domain.model.Candidate
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime

@AndroidEntryPoint
class AddUpdateScreen : AppCompatActivity() {


    private lateinit var binding: AddCandidateBinding

    private val viewModel: AddUpdateViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddCandidateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.addUpdateCandidate) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSaveCandidate()
        setReturnBack()
    }

    private fun setReturnBack() {
        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }
    }


    private fun setSaveCandidate() {
        var image: Uri? = null

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

            val day = binding.datePicker.dayOfMonth
            val month =
                binding.datePicker.month + 1 //month is 0 indexed, add 1 to get the correct month
            val year = binding.datePicker.year

            val dateTime = LocalDateTime.of(year, month, day, 0, 0)


            //salary, notes can be empty, set 0.0 and empty string if so
            val salary =
                if (binding.salary.text.toString().isEmpty()) 0.0 else binding.salary.text.toString()
                    .toDouble()
            val notes =
                binding.notes.text.toString().ifEmpty { "" }


            val candidate = Candidate(
                firstName = binding.firstName.text.toString(),
                lastName = binding.lastName.text.toString(),
                email = binding.email.text.toString(),
                phoneNumber = binding.phone.text.toString(),
                birthday = dateTime,
                salaryClaim = salary,
                notes = notes,
                image = image ?: Uri.parse("")
            )

            viewModel.addCandidate(candidate)
            finish()
        }
    }
    private fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

}