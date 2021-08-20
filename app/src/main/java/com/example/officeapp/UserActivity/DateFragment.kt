package com.example.officeapp

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orhanobut.hawk.Hawk

class DateFragment() : Fragment()
{
    private var fragmentView:View?=null
    private val viewModel:DateViewModel by viewModels()
    private var participantsRVAdapter:ParticipantsRecyclerViewAdapter?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentView=inflater.inflate(Hawk.get(Constants.DATE_LAYOUT) as Int, container, false)

        if(arguments?.getSerializable(Constants.CHOSEN_DATE)!=null)
            viewModel.setChosenDate(arguments?.getSerializable(Constants.CHOSEN_DATE) as Date)

        val userName:String= Hawk.get(Constants.LOGGEDIN_USERNAME)
        val userID:String= Hawk.get(Constants.LOGGEDIN_USERID)

        setParticipantRecyclerView()

        viewModel.getChosenDate().observe(viewLifecycleOwner, {
            val userNameList=ArrayList<String>()
            val userMap=viewModel.getChosenDate().value!!.getUsers()
            for(user in userMap)
                userNameList.add(user.value)
            participantsRVAdapter?.setNewParticipants(userNameList)
            participantsRVAdapter?.notifyDataSetChanged()
            setDateInfo()
        })

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

        val editCapacityIcon=fragmentView?.findViewById<ImageView>(R.id.editCapacityIcon)
        editCapacityIcon?.setOnClickListener{
            editCapacity()
        }

        return fragmentView
    }

    private fun editCapacity() {
        val dialog = Dialog(fragmentView?.context!!)
        dialog?.setContentView(R.layout.edit_capacity_dialog)
        dialog?.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val capacityPicker = dialog?.findViewById<NumberPicker>(R.id.capacityPicker)
        capacityPicker?.maxValue = 1000
        capacityPicker?.minValue = 0
        capacityPicker?.value=viewModel?.getChosenDate().value!!.getCap()

        val saveButton = dialog.findViewById<LinearLayout>(R.id.saveSettingsButton)
        saveButton?.setOnClickListener {
            viewModel.changeCapacity(capacityPicker?.value!!)
            dialog.dismiss()
        }
    }

    private fun setParticipantRecyclerView()
    {
        val participantsRV = fragmentView?.findViewById<RecyclerView>(R.id.participantsRV)
        participantsRV?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        participantsRVAdapter = ParticipantsRecyclerViewAdapter(activity?.applicationContext!!)
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