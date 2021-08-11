package com.example.officeapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ParticipatedDaysRVAdapter(val participatedDays:ArrayList<String>, val context: Context) :
    RecyclerView.Adapter<ParticipatedDaysRVAdapter.ViewHolder>()
{
    class ViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val participatedDate=view.findViewById<TextView>(R.id.participatedDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.participated_dates_rv_bg, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.participatedDate.text="${position+1}-) "+participatedDays.get(position)
    }

    override fun getItemCount(): Int {
        return participatedDays.size
    }
}