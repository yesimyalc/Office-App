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

class UserHomePageFragment(val connector:ActivityFragmentConnector): Fragment(R.layout.fragment_employee_home_page), DateFragmentConnector
{
    private var fragmentView:View?=null
    private var dateRVAdapter:CalendarRecyclerViewAdapter?=null
    private val viewModel:UserHomePageViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        fragmentView=super.onCreateView(inflater, container, savedInstanceState)

        val allCurrentDates=fragmentView?.findViewById<TextView>(R.id.allCurrentDates)
        setDateRecyclerView()

        viewModel.getDateList().observe(viewLifecycleOwner, {
            if(viewModel.getDateList().value?.size==7)
                 allCurrentDates?.text="${viewModel.getDateList().value!![0].toStringText()} - ${viewModel.getDateList().value!![6].toStringText()}"
            dateRVAdapter?.setDates(viewModel.getDateList().value!!)
            dateRVAdapter?.notifyDataSetChanged()
        })

        return fragmentView
    }

    private fun setDateRecyclerView()
    {
        val daysRView = fragmentView?.findViewById<RecyclerView>(R.id.daysRView)
        daysRView?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        dateRVAdapter = CalendarRecyclerViewAdapter(activity?.applicationContext!!, this)
        dateRVAdapter?.setDates(viewModel.getDateList().value!!)
        daysRView?.adapter = dateRVAdapter
    }

    override fun onDate(date: Date) {
        connector.onShowDateInfo(date)
    }
}