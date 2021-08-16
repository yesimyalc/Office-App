package com.example.officeapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent
import android.text.Layout
import android.widget.GridLayout
import androidx.recyclerview.widget.GridLayoutManager
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*
import kotlin.collections.HashSet

class UserHomePageFragment(): Fragment(R.layout.fragment_employee_home_page), DateFragmentConnector
{
    private var fragmentView:View?=null
    private var dateRVAdapter2:CalendarRecyclerViewAdapter2?=null
    private val viewModel:UserHomePageViewModel by viewModels()
    private var connector:ActivityFragmentConnector?=null
    private var dateList=ArrayList<Date>()

    companion object {
        fun newInstance(connector: ActivityFragmentConnector): UserHomePageFragment {

            val fragment = UserHomePageFragment()
            fragment.setConnector(connector)
            return fragment
        }
    }

    fun setConnector(connector: ActivityFragmentConnector)
    {
        this.connector=connector
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        fragmentView=super.onCreateView(inflater, container, savedInstanceState)

        val calendarInfo=fragmentView?.findViewById<TextView>(R.id.calendarInfo)
        if(dateList.isEmpty())
            setDates()
        calendarInfo?.text=dateList.get(10).getMonthString()+" "+dateList.get(10).year

        val allCurrentDates=fragmentView?.findViewById<TextView>(R.id.allCurrentDates)
        setDateRecyclerView2()

        viewModel.getDateList().observe(viewLifecycleOwner, {
            if(viewModel.getDateList().value?.size==7)
                 allCurrentDates?.text="${viewModel.getDateList().value!![0].toStringText()} - ${viewModel.getDateList().value!![6].toStringText()}"
            for(i in 0..dateList.size-1)
                for (updatedDate in viewModel.getDateList().value!!)
                    if (dateList.get(i).equals(updatedDate))
                        dateList[i] = updatedDate

            dateRVAdapter2?.setBoundaries(viewModel.getDateList().value!!)
            dateRVAdapter2?.setDates(dateList)
            dateRVAdapter2?.notifyDataSetChanged()

        })

        return fragmentView
    }

    private fun setDates()
    {
        val month=Date().month
        val year=Date().year
        val start= Calendar.getInstance()
        start.set(MONTH, month)

        start.set(YEAR, year+1900)
        start.set(DAY_OF_MONTH, 1)
        start.getTime()

        start.set(DAY_OF_WEEK, SUNDAY)
        if (start.get(MONTH) <= month)
            start.add(DATE, -7)

        val end = Calendar.getInstance()
        end.set(MONTH, month+1)
        end.set(YEAR, year+1900)
        end.set(DAY_OF_MONTH, 1)
        end.getTime()
        end.set(DATE, -1)
        end.set(DAY_OF_WEEK, SATURDAY)
        if (end.get(MONTH) != month+1)
            end.add(DATE, + 7)

        var date=Date(start.time.date, start.time.month, start.time.year+1900, 10)
        dateList.add(date)
        while (start.before(end))
        {
            start.add(DATE, 1)
            date=Date(start.time.date, start.time.month, start.time.year+1900, 10)
            dateList.add(date)
        }
    }

    private fun setDateRecyclerView2()
    {
        val daysRView = fragmentView?.findViewById<RecyclerView>(R.id.calendarGridView)
        daysRView?.layoutManager = GridLayoutManager(activity, 7)
        dateRVAdapter2 = CalendarRecyclerViewAdapter2(activity?.applicationContext!!, this)
        dateRVAdapter2?.setDates(dateList)
        daysRView?.adapter = dateRVAdapter2
    }

    override fun onDate(date: Date) {
        connector?.onShowDateInfo(date)
    }
}