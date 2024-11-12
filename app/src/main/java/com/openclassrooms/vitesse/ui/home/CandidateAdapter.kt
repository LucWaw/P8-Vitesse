package com.openclassrooms.vitesse.ui.home

import android.content.res.Resources
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.vitesse.R
import com.openclassrooms.vitesse.domain.model.Candidate

class CandidateAdapter(allItemsFragment: AllItemsFragment) :
    ListAdapter<Candidate, CandidateAdapter.CandidateViewHolder>(
        DIFF_CALLBACK
    ) {
    inner class CandidateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name:TextView
        var notes:TextView
        var image: ImageView
        
        init {
            name = itemView.findViewById(R.id.name)
            notes = itemView.findViewById(R.id.notes)
            image = itemView.findViewById(R.id.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidateViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_candidate, parent, false)
        return CandidateViewHolder(itemView)    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CandidateViewHolder, position: Int) {
        val candidate = getItem(position)

        holder.name.text = Resources.getSystem().getString(R.string.candidateName, candidate.firstName, candidate.lastName.uppercase() )

        holder.notes.text = candidate.notes

        //Set image from candidates.image URI
        holder.image.setImageURI(candidate.image)
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
