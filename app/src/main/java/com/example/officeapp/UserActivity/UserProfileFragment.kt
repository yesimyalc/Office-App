package com.example.officeapp

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
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
            dialog?.show()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

            val newUserNick=dialog.findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.newUserNick)
            val newUserPass=dialog.findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.newUserPass)

            newUserNick.setText(viewModel?.getLoggedInUser().value?.getUserNickname())
            newUserPass.setText(viewModel?.getLoggedInUser().value?.getUserPassword())

            val saveSettingButton=dialog.findViewById<LinearLayout>(R.id.saveSettingsButton)
            saveSettingButton?.setOnClickListener {
                val text=viewModel?.editInfo(newUserNick.text.toString(), newUserPass.text.toString())
                if(text!=null)
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                else
                    dialog.dismiss()
            }
        }

        val logoutButton=fragmentView?.findViewById<androidx.cardview.widget.CardView>(R.id.logoutButton)
        logoutButton?.setOnClickListener {
            val dialog= Dialog(fragmentView?.context!!)
            dialog?.setContentView(R.layout.logout_dialog)
            dialog?.show()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

            val noButton=dialog.findViewById<LinearLayout>(R.id.noButton)
            noButton.setOnClickListener {
                dialog.dismiss()
            }
            val yesButton=dialog.findViewById<LinearLayout>(R.id.yesButton)
            yesButton.setOnClickListener {
                val intent= Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent)
                activity?.finish()
            }
        }

        return fragmentView
    }

    private fun setupUserInfo()
    {
        val profileNick=fragmentView?.findViewById<TextView>(R.id.profileNick)
        val profileName=fragmentView?.findViewById<TextView>(R.id.profileName)
        val profilePass=fragmentView?.findViewById<TextView>(R.id.profilePass)
        val userStatus=fragmentView?.findViewById<TextView>(R.id.userStatus)

        val name = SpannableStringBuilder().bold { append("User Name: ") }.append(viewModel.getLoggedInUser().value?.getUserName())
        val pass= SpannableStringBuilder().bold { append("User Password: ") }.append(viewModel.getLoggedInUser().value?.getUserPassword())

        profileNick?.text=viewModel.getLoggedInUser().value?.getUserNickname()
        profileName?.text=name
        profilePass?.text=pass
        if(viewModel.getLoggedInUser().value!! is Employee)
            userStatus?.text="Employee"
        else
            userStatus?.text="Admin"
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