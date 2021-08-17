package com.example.officeapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class UserProfileViewModel(val state: SavedStateHandle): ViewModel(),ProfileViewRepConnector
{
    private val loggedInUser:MutableLiveData<User> =state.getLiveData(Constants.LOGGEDIN_USER)
        fun getLoggedInUser():LiveData<User>{return loggedInUser}

    private var repository:UserProfileRepository?=null

    private val editState=MutableLiveData<String>()
        fun getEditState():LiveData<String>{return editState}

    fun setLoggedInUserID(userID:String)
    {
        repository=UserProfileRepository(userID, this)
    }

    override fun onRetrieveUser(user: User) {
        loggedInUser.value=user
    }

    override fun onChangeEditState(editState: String) {
        this.editState.value=editState
    }

    fun deleteDay(day:String)
    {
        loggedInUser.value=loggedInUser.value
        repository?.deleteUserFromDate(day)
    }

    fun editInfo(nick: String, pass:String)
    {
        if(nick.isNullOrEmpty() || pass.isNullOrEmpty()) {
            editState.value = "Enter information first."
            return
        }
        repository?.editUserInfo(nick, pass)
    }
}