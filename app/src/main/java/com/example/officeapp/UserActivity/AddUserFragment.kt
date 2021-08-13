package com.example.officeapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

class AddUserFragment: Fragment(R.layout.fragment_add_user)
{
    private var fragmentView:View?=null
    private val viewModel:AddUserViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        fragmentView=super.onCreateView(inflater, container, savedInstanceState)

        val newUserNickname=fragmentView?.findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.newUserNick)
        val newUserName=fragmentView?.findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.newUserName)
        val newUserPass=fragmentView?.findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.newUserPass)
        val newUserAdmin=fragmentView?.findViewById<RadioButton>(R.id.newUserAdmin)
        val newUserEmployee=fragmentView?.findViewById<RadioButton>(R.id.newUserEmployee)

        val addUserButton=fragmentView?.findViewById<LinearLayout>(R.id.addUserButton)
        addUserButton?.setOnClickListener {

            viewModel.addUser(newUserNickname?.text.toString(), newUserName?.text.toString(), newUserPass?.text.toString(),
                newUserAdmin?.isChecked!!, newUserEmployee?.isChecked!!)

        }

        viewModel.getState().observe(viewLifecycleOwner, {
            if(viewModel.getState().value!="")
                Toast.makeText(context, viewModel.getState().value, Toast.LENGTH_SHORT).show()
            if(viewModel.getState().value=="New user is added." || viewModel.getState().value=="User already exists.")
            {
                newUserName?.text?.clear()
                newUserNickname?.text?.clear()
                newUserPass?.text?.clear()
                newUserName?.clearFocus()
                newUserNickname?.clearFocus()
                newUserPass?.clearFocus()
                newUserAdmin?.isChecked=false
                newUserEmployee?.isChecked=false
            }
        })

        return fragmentView
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.setState("")
    }
}