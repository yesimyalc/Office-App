package com.example.officeapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class UserProfileViewModel(val state: SavedStateHandle): ViewModel(),ProfileViewRepConnector
{
    private val loggedInUserID:MutableLiveData<String> =state.getLiveData<String>(Constants.LOGGEDIN_USERID)
        fun getLoggedInUserID():LiveData<String>{return loggedInUserID}

    private var loggedInUser:MutableLiveData<User> =state.getLiveData(Constants.LOGGEDIN_USER)
        fun getLoggedInUser():LiveData<User>{return loggedInUser}

    private var repository:UserProfileRepository?=null

    fun setLoggedInUserID(userID:String)
    {
        loggedInUserID.value=userID
        repository=UserProfileRepository(loggedInUserID.value!!, this)
    }

    override fun onRetrieveUser(user: User) {
        loggedInUser.value=user
    }
}