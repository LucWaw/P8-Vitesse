package com.openclassrooms.vitesse.ui.home

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.vitesse.databinding.ItemCandidateBinding
import com.openclassrooms.vitesse.domain.model.Candidate

class CandidateAdapter() :
    ListAdapter<Candidate, CandidateAdapter.CandidateViewHolder>(
        DIFF_CALLBACK
    ) {

    inner class CandidateViewHolder(binding: ItemCandidateBinding) : RecyclerView.ViewHolder(binding.root) {
        var name:TextView = binding.name
        var notes:TextView = binding.notes
        var image: ImageView = binding.imageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidateViewHolder {
        val binding = ItemCandidateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CandidateViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")//No need translation
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CandidateViewHolder, position: Int) {
        val candidate = getItem(position)

        holder.name.text = candidate.firstName + " " + candidate.lastName.uppercase()

        holder.notes.text = candidate.notes

        //Set image from candidates.image URI
        if (candidate.image.toString() != "") {
            holder.image.setImageURI(candidate.image)
        }

    }



    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Candidate> =
            object : DiffUtil.ItemCallback<Candidate>() {
                override fun areItemsTheSame(oldItem: Candidate, newItem: Candidate): Boolean {
                    return oldItem === newItem
                }

                override fun areContentsTheSame(oldItem: Candidate, newItem: Candidate): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
