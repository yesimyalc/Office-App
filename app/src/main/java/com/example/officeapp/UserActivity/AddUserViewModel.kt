package com.example.officeapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddUserViewModel(): ViewModel(),AddUserRepViewModelConnector
{
    private val repository=AddUserRepository(this)
    private val state=MutableLiveData<AddRemoveUserState>()
        fun getState():LiveData<AddRemoveUserState>{return state}

    private val section=MutableLiveData<String>()
        fun getSection():LiveData<String>{return section}
        fun setSection(newSection:String){
            section.value=newSection
        }

    fun addUser(nick:String, name:String, pass:String, isAdmin:Boolean, isEmployee:Boolean)
    {
        if(nick.isNullOrEmpty() || name.isNullOrEmpty() || pass.isNullOrEmpty())
            state.value=AddRemoveUserState.WAITING_INFO
        else if(isAdmin==false && isEmployee==false)
            state.value=AddRemoveUserState.WAITING_STATUS
        else
            repository.addUser(nick, name, pass, isAdmin)

    }

    fun removeUser(nick:String, currentNick:String)
    {
        if(nick.isNullOrEmpty())
            state.value=AddRemoveUserState.WAITING_INFO
        else if(nick==currentNick)
            state.value=AddRemoveUserState.CURRENT_ACCOUNT
        else
            repository.removeUser(nick, true)
    }

    override fun setState(state: AddRemoveUserState) {
        this.state.value=state
    }

    fun removeAccount(currentNick: String)
    {
        repository.removeUser(currentNick, false)
    }

}