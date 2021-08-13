package com.example.officeapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DateFragment : Fragment(R.layout.fragment_date)
{
    private var fragmentView:View?=null
    private val viewModel:DateViewModel by viewModels()
    private var participantsRVAdapter:ParticipantsRecyclerViewAdapter?=null
    var isActive=false
    private val userNameList=ArrayList<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentView=super.onCreateView(inflater, container, savedInstanceState)

        viewModel.setChosenDate(arguments?.getSerializable(Constants.CHOSEN_DATE) as Date)
        val userName=arguments?.getString(Constants.LOGGEDIN_USERNAME)!!
        val userID=arguments?.getString(Constants.LOGGEDIN_USERID)!!

        viewModel.getChosenDate().observe(viewLifecycleOwner, {
            userNameList.clear()
            val userMap=viewModel.getChosenDate().value!!.getUsers()
            for(user in userMap)
                userNameList.add(user.value)
            participantsRVAdapter?.notifyDataSetChanged()
            setDateInfo()
            if(viewModel.getChosenDate().value!=null)
                isActive=true
        })

        setParticipantRecyclerView()

        val participateButton=fragmentView?.findViewById<LinearLayout>(R.id.participateButton)
        participateButton?.setOnClickListener {
            if(viewModel.getChosenDate().value?.getCap()==viewModel.getChosenDate().value?.getCurrentPeopleAmount())
                Toast.makeText(activity?.applicationContext, "The capacity of this date is maxed. You cannot participate.", Toast.LENGTH_SHORT).show()
            else if(viewModel.getChosenDate().value?.isParticipant(userID)==false)
                viewModel.addUser(userID, userName)
            else
                Toast.makeText(activity?.applicationContext, "You are already a participant.", Toast.LENGTH_SHORT).show()
        }

        val cancelParticipateButton=fragmentView?.findViewById<LinearLayout>(R.id.cancelParticipateButton)
        cancelParticipateButton?.setOnClickListener{
            if(viewModel.getChosenDate().value?.isParticipant(userID)==false)
                Toast.makeText(activity?.applicationContext, "You are already not a participant.", Toast.LENGTH_SHORT).show()
            else
                viewModel.deleteUser(userID)
        }

        return fragmentView
    }

    private fun setParticipantRecyclerView()
    {
        val participantsRV = fragmentView?.findViewById<RecyclerView>(R.id.participantsRV)
        participantsRV?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        participantsRVAdapter = ParticipantsRecyclerViewAdapter(userNameList, activity?.applicationContext!!)
        participantsRV?.adapter = participantsRVAdapter
    }

    private fun setDateInfo()
    {
        val dateInfo=fragmentView?.findViewById<LinearLayout>(R.id.dateInfo)
        when(viewModel.getChosenDate().value?.getState())
        {
            "Mostly Available"->{dateInfo?.setBackgroundResource(R.color.transparent_teal)}
            "Mostly Full"->{dateInfo?.setBackgroundResource(R.color.transparent_blue)}
            "Full"->{dateInfo?.setBackgroundResource(R.color.transparent_dark_blue)}
        }

        val currentDate=fragmentView?.findViewById<TextView>(R.id.currentDate)
        currentDate?.text=""+viewModel.getChosenDate().value?.date+" "+viewModel.getChosenDate().value?.getMonthString()+" "+viewModel.getChosenDate().value?.getWeekDayString()

        val currentDateCapacity=fragmentView?.findViewById<TextView>(R.id.currentDateCapacity)
        val cap=viewModel.getChosenDate().value?.getCap()
        val currentPeople=viewModel.getChosenDate().value?.getCurrentPeopleAmount()
        currentDateCapacity?.text="Capacity: $cap - Available: ${cap!!-currentPeople!!}"
    }
}