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
import com.orhanobut.hawk.Hawk

class UserProfileFragment: Fragment(R.layout.fragment_employee_profile), ProfileDatesRVConnector
{
    private val viewModel:UserProfileViewModel by viewModels()
    private var fragmentView:View?=null
    private var participatedDaysRVAdapter:ParticipatedDaysRVAdapter?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        fragmentView=super.onCreateView(inflater, container, savedInstanceState)

        setupAdapters()

        viewModel.getLoggedInUser().observe(viewLifecycleOwner, {
            setupUserInfo()
        })

        viewModel.getUsersParticipatedDays().observe(viewLifecycleOwner, {
            participatedDaysRVAdapter?.setDays(viewModel.getUsersParticipatedDays().value!!)
            participatedDaysRVAdapter?.notifyDataSetChanged()
        })

        val dialog= Dialog(requireContext())
        val editInfoButton=fragmentView?.findViewById<androidx.cardview.widget.CardView>(R.id.editButton)
        editInfoButton?.setOnClickListener {
            editInfo(dialog)
        }

        viewModel?.getEditState().observe(viewLifecycleOwner,{
            if(viewModel.getEditState().value!=null )
            {
                if(viewModel.getEditState().value!=EditProfileStates.REFRESHED)
                    Toast.makeText(requireContext(), viewModel.getEditState().value?.text, Toast.LENGTH_SHORT).show()

                if(viewModel.getEditState().value==EditProfileStates.SUCCESSFUL)
                    dialog.dismiss()
            }
        })

        val logoutButton=fragmentView?.findViewById<androidx.cardview.widget.CardView>(R.id.logoutButton)
        logoutButton?.setOnClickListener {
            logout()
        }

        return fragmentView
    }

    override fun onDestroyView() {
        viewModel.onChangeEditState(EditProfileStates.REFRESHED)
        super.onDestroyView()
    }

    private fun editInfo(dialog:Dialog)
    {
        dialog?.setContentView(R.layout.edit_info_dialog)
        dialog?.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        val newUserNick=dialog.findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.newUserNick)
        val newUserPass=dialog.findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.newUserPass)

        newUserNick.setText(viewModel?.getLoggedInUser().value?.getUserNickname())
        newUserPass.setText(viewModel?.getLoggedInUser().value?.getUserPassword())

        Hawk.put(Constants.LOGGEDIN_PASS, viewModel?.getLoggedInUser().value?.getUserPassword())
        Hawk.put(Constants.LOGGEDIN_USERNICK, viewModel?.getLoggedInUser().value?.getUserNickname())

        val saveSettingButton=dialog.findViewById<LinearLayout>(R.id.saveSettingsButton)
        saveSettingButton?.setOnClickListener {
            viewModel?.editInfo(newUserNick.text.toString(), newUserPass.text.toString())
        }
    }

    private fun logout()
    {
        val dialog= Dialog(requireContext())
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

            Hawk.delete(Constants.LOGGEDIN_USERNICK)
            Hawk.delete(Constants.LOGGEDIN_PASS)
            Hawk.delete(Constants.LOGGEDIN_USERNAME)
            Hawk.delete(Constants.LOGGEDIN_USERID)
            dialog.dismiss()

            startActivity(intent)
            activity?.finish()
        }
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
        participatedDaysRVAdapter = ParticipatedDaysRVAdapter(activity?.applicationContext!!, this)
        participatedRV?.adapter = participatedDaysRVAdapter
    }

    override fun onDelete(day: String) {
        viewModel.deleteDay(day)
    }

}