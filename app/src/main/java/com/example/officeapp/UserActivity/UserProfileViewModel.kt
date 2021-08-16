package com.example.officeapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class UserProfileViewModel(val state: SavedStateHandle): ViewModel(),ProfileViewRepConnector
{
    private var loggedInUser:MutableLiveData<User> =state.getLiveData(Constants.LOGGEDIN_USER)
        fun getLoggedInUser():LiveData<User>{return loggedInUser}

    private var repository:UserProfileRepository?=null

    fun setLoggedInUserID(userID:String)
    {
        repository=UserProfileRepository(userID, this)
    }

    override fun onRetrieveUser(user: User) {
        loggedInUser.value=user
    }

    fun deleteDay(day:String)
    {
        loggedInUser.value=loggedInUser.value
        repository?.deleteUserFromDate(day)
    }

    fun editInfo(nick: String, pass:String):String?
    {
        if(nick.isNullOrEmpty() || pass.isNullOrEmpty())
            return "Enter information first."
        repository?.editUserInfo(nick, pass)

        return null
    }
}