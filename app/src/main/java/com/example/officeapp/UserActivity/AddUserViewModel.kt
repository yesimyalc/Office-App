package com.example.officeapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddUserViewModel(): ViewModel(),AddUserRepViewModelConnector
{
    private val repository=AddUserRepository(this)
    private val state=MutableLiveData<String>()
        fun getState():LiveData<String>{return state}

    fun addUser(nick:String, name:String, pass:String, isAdmin:Boolean, isEmployee:Boolean)
    {
        if(nick.isNullOrEmpty() || name.isNullOrEmpty() || pass.isNullOrEmpty())
            state.value="Enter user information first."
        else if(isAdmin==false && isEmployee==false)
            state.value="Select admin or employee."
        else
            repository.addUser(nick, name, pass, isAdmin)

    }

    fun removeUser(nick:String, currentNick:String)
    {
        if(nick.isNullOrEmpty())
            state.value="Enter user information first."
        else if(nick==currentNick)
            state.value="Removing current account."
        else
            repository.removeUser(nick, true)
    }

    override fun setState(state: String) {
        this.state.value=state
    }

    fun removeAccount(currentNick: String)
    {
        repository.removeUser(currentNick, false)
    }

}