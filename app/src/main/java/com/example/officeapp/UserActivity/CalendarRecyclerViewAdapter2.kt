package com.example.officeapp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarRecyclerViewAdapter2(val context: Context, val connector:DateFragmentConnector):RecyclerView.Adapter<CalendarRecyclerViewAdapter2.ViewHolder>()
{
    private var dates:ArrayList<Date>?=null
    private var boundaryList:ArrayList<Date>?=null

    class ViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        val currentDate=view.findViewById<LinearLayout>(R.id.date)
        val dateInfo=view.findViewById<TextView>(R.id.dateInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.calendar_item_bg, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.dateInfo.text=dates!!.get(position).date.toString()

        if(boundaryList!!.contains(dates!!.get(position)))
        {
            holder.currentDate.setOnClickListener {
                connector.onDate(dates!!.get(position))
            }
        }

        if(!(boundaryList!!.contains(dates!!.get(position))))
            holder.currentDate.setBackgroundResource(R.color.transparent_white4)
        else
        {
            when (dates!!.get(position).getState())
            {
                "Mostly Available" -> { holder.currentDate.setBackgroundResource(R.color.teal) }
                "Mostly Full" -> { holder.currentDate.setBackgroundResource(R.color.blue) }
                "Full" -> { holder.currentDate.setBackgroundResource(R.color.dark_blue) }
            }
        }
    }

    override fun getItemCount(): Int {
        return dates?.size!!
    }

    fun setDates(list:ArrayList<Date>)
    {
        dates=list
    }

    fun setBoundaries(list:ArrayList<Date>)
    {
        boundaryList=list
    }

}