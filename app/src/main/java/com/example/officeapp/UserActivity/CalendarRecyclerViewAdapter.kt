package com.example.officeapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarRecyclerViewAdapter(val context: Context, val connector:DateFragmentConnector):RecyclerView.Adapter<CalendarRecyclerViewAdapter.ViewHolder>()
{
    private var dates:ArrayList<Date>?=null

    class ViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        val dateText=view.findViewById<TextView>(R.id.dateText)
        val dateColor=view.findViewById<LinearLayout>(R.id.dateColor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.days_recycler_view_bg, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dayNoInt=dates?.get(position)?.date!!
        var dayNoString:String=dayNoInt.toString()
        if(dayNoInt<10)
            dayNoString="0"+dayNoString

        val text=""+dates?.get(position)?.getMonthString()+" "+dayNoString+"\n"+dates?.get(position)?.getWeekDayString()
        holder.dateText.text=text

        holder.dateColor.setOnClickListener {
            connector.onDate(dates!!.get(position))
        }

        when(dates!!.get(position).getState())
        {
            "Mostly Available"->{holder.dateColor.setBackgroundResource(R.color.teal)}
            "Mostly Full"->{holder.dateColor.setBackgroundResource(R.color.blue)}
            "Full"->{holder.dateColor.setBackgroundResource(R.color.dark_blue)}
        }
    }

    override fun getItemCount(): Int {
        return dates?.size!!
    }

    fun setDates(list:ArrayList<Date>)
    {
        dates=list
    }

}