package com.example.officeapp

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.bold
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class UserProfileFragment: Fragment(R.layout.fragment_employee_profile), ProfileDatesRVConnector
{
    private val viewModel:UserProfileViewModel by viewModels()
    private var fragmentView:View?=null
    private var participatedDaysRVAdapter:ParticipatedDaysRVAdapter?=null
    private var dates=ArrayList<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        fragmentView=super.onCreateView(inflater, container, savedInstanceState)
        viewModel.setLoggedInUserID(arguments?.getString(Constants.LOGGEDIN_USERID)!!)

        setupAdapters()

        viewModel.getLoggedInUser().observe(viewLifecycleOwner, {
            dates.clear()
            dates.addAll(viewModel.getLoggedInUser().value?.getParticipatedDays()!!)
            participatedDaysRVAdapter?.notifyDataSetChanged()
            setupUserInfo()
        })

        val editInfoButton=fragmentView?.findViewById<androidx.cardview.widget.CardView>(R.id.editButton)
        editInfoButton?.setOnClickListener {
            val dialog= Dialog(fragmentView?.context!!)
            dialog?.setContentView(R.layout.edit_info_dialog)
            dialog?.setTitle("Add New Movie")
            dialog?.show()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        }

        return fragmentView
    }

    private fun setupUserInfo()
    {
        val profileNick=fragmentView?.findViewById<TextView>(R.id.profileNick)
        val profileName=fragmentView?.findViewById<TextView>(R.id.profileName)
        val profilePass=fragmentView?.findViewById<TextView>(R.id.profilePass)

        val name = SpannableStringBuilder().bold { append("User Name: ") }.append(viewModel.getLoggedInUser().value?.getUserName())
        val pass= SpannableStringBuilder().bold { append("User Password: ") }.append(viewModel.getLoggedInUser().value?.getUserPassword())

        profileNick?.text=viewModel.getLoggedInUser().value?.getUserNickname()
        profileName?.text=name
        profilePass?.text=pass
    }

    private fun setupAdapters()
    {
        val participatedRV = fragmentView?.findViewById<RecyclerView>(R.id.participatedDatesRV)
        participatedRV?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        participatedDaysRVAdapter = ParticipatedDaysRVAdapter(dates, activity?.applicationContext!!, this)
        participatedRV?.adapter = participatedDaysRVAdapter
    }

    override fun onDelete(day: String) {
        viewModel.deleteDay(day)
    }

}