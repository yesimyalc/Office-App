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
    private var dateRVAdapter:CalendarRecyclerViewAdapter2?=null
    private val viewModel:UserHomePageViewModel by viewModels()
    private var connector:ActivityFragmentConnector?=null

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
        val calendar=Calendar.getInstance()
        calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US)
        calendarInfo?.text=calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US)+" "+calendar.get(Calendar.YEAR).toString()

        setDateRecyclerView()

        val allCurrentDates=fragmentView?.findViewById<TextView>(R.id.allCurrentDates)
        viewModel.getBoundaryList().observe(viewLifecycleOwner, {
            allCurrentDates?.text="${viewModel.getBoundaryList().value!![0].toStringText()} - ${viewModel.getBoundaryList().value!![6].toStringText()}"
        })

        viewModel.getDateList().observe(viewLifecycleOwner, {
            dateRVAdapter?.setBoundaries(viewModel.getBoundaryList().value!!)
            dateRVAdapter?.setDates(viewModel.getDateList().value!!)
            dateRVAdapter?.notifyDataSetChanged()

        })

        return fragmentView
    }

    private fun setDateRecyclerView()
    {
        val daysRView = fragmentView?.findViewById<RecyclerView>(R.id.calendarGridView)
        daysRView?.layoutManager = GridLayoutManager(requireActivity(), 7)
        dateRVAdapter = CalendarRecyclerViewAdapter2(requireContext(), this)
        dateRVAdapter?.setDates(viewModel.getDateList().value!!)
        daysRView?.adapter = dateRVAdapter
    }

    override fun onDate(date: Date) {
        connector?.onShowDateInfo(date)
    }
}