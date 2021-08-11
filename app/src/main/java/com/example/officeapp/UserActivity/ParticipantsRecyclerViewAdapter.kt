package com.example.officeapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ParticipantsRecyclerViewAdapter(val participants:ArrayList<String>, val context: Context) :RecyclerView.Adapter<ParticipantsRecyclerViewAdapter.ViewHolder>()
{
    class ViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val participantName=view.findViewById<TextView>(R.id.participantName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ParticipantsRecyclerViewAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.participants_recycler_view_bg, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.participantName.text="${position+1}-) "+participants.get(position)
    }

    override fun getItemCount(): Int {
        return participants.size
    }
}