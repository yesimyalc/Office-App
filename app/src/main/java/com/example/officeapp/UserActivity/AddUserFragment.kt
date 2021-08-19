package com.example.officeapp

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.orhanobut.hawk.Hawk

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
        val removedUserNick=fragmentView?.findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.removedUserNick)

        val addUserBar=fragmentView?.findViewById<RadioButton>(R.id.addUserBar)
        val removeUserBar=fragmentView?.findViewById<RadioButton>(R.id.removeUserBar)
        addUserBar?.setOnClickListener {
            switchSection()
        }
        removeUserBar?.setOnClickListener {
            switchSection()
        }

        val addUserButton=fragmentView?.findViewById<LinearLayout>(R.id.addUserButton)
        addUserButton?.setOnClickListener {

            viewModel.addUser(newUserNickname?.text.toString(), newUserName?.text.toString(), newUserPass?.text.toString(),
                newUserAdmin?.isChecked!!, newUserEmployee?.isChecked!!)
        }

        val removeUserButton=fragmentView?.findViewById<LinearLayout>(R.id.removeUserButton)
        removeUserButton?.setOnClickListener {
            viewModel.removeUser(removedUserNick?.text.toString(), Hawk.get(Constants.LOGGEDIN_USERNICK))
        }

        viewModel.getState().observe(viewLifecycleOwner, {
            if(viewModel.getState().value!=AddRemoveUserState.REFRESHED && viewModel.getState().value!=AddRemoveUserState.CURRENT_ACCOUNT)
                Toast.makeText(context, viewModel.getState().value?.text, Toast.LENGTH_SHORT).show()

            if(viewModel.getState().value==AddRemoveUserState.USER_ADDED || viewModel.getState().value==AddRemoveUserState.EXISTING_USER)
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
            else if(viewModel.getState().value==AddRemoveUserState.NON_EXISTING_USER || viewModel.getState().value==AddRemoveUserState.USER_REMOVED) {
                removedUserNick?.text?.clear()
                removedUserNick?.clearFocus()
            }
            else if(viewModel.getState().value==AddRemoveUserState.CURRENT_ACCOUNT)
                removeCurrentAccount()
        })

        return fragmentView
    }

    private fun removeCurrentAccount()
    {
        val dialog= Dialog(fragmentView?.context!!)
        dialog?.setContentView(R.layout.remove_account_dialog)
        dialog?.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        val noButton=dialog.findViewById<LinearLayout>(R.id.noButton)
        noButton.setOnClickListener {
            dialog.dismiss()
        }
        val yesButton=dialog.findViewById<LinearLayout>(R.id.yesButton)
        yesButton.setOnClickListener {

            viewModel.removeAccount(Hawk.get(Constants.LOGGEDIN_USERNICK))

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

    override fun onDestroy() {
        super.onDestroy()
        viewModel.setState(AddRemoveUserState.REFRESHED)
    }

    fun switchSection()
    {
        val addUserBar=fragmentView?.findViewById<RadioButton>(R.id.addUserBar)
        val addUserLayout=fragmentView?.findViewById<LinearLayout>(R.id.addUserLayout)
        val removeUserLayout=fragmentView?.findViewById<LinearLayout>(R.id.removeUserLayout)

        if(addUserBar?.isChecked!!)
        {
            removeUserLayout?.visibility=View.GONE
            addUserLayout?.visibility=View.VISIBLE
        }
        else
        {
            removeUserLayout?.visibility=View.VISIBLE
            addUserLayout?.visibility=View.GONE
        }
    }
}