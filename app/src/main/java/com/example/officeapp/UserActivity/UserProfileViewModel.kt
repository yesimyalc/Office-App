package com.example.officeapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.orhanobut.hawk.Hawk

class UserProfileViewModel(val state: SavedStateHandle): ViewModel(),ProfileViewRepConnector
{
    private var loggedInUser=MutableLiveData<User>()
        fun getLoggedInUser():LiveData<User>{return loggedInUser}

    private val usersParticipatedDays=MutableLiveData<ArrayList<String>>()
        fun getUsersParticipatedDays():LiveData<ArrayList<String>>{return usersParticipatedDays}

    private var repository:UserProfileRepository?=null

    private val editState=MutableLiveData<EditProfileStates>()
        fun getEditState():LiveData<EditProfileStates>{return editState}

    init{
        setLoggedInUserID(Hawk.get(Constants.LOGGEDIN_USERID))
    }

    fun setLoggedInUserID(userID:String)
    {
        repository=UserProfileRepository(userID, this)
    }

    override fun onRetrieveUser(user: User) {
        loggedInUser.value=user
        usersParticipatedDays.value=loggedInUser.value?.getParticipatedDays()
    }

    override fun onChangeEditState(editState: EditProfileStates) {
        this.editState.value=editState
    }

    fun deleteDay(day:String)
    {
        repository?.deleteUserFromDate(day)
    }

    fun editInfo(nick: String, pass:String)
    {
        if(nick.isNullOrEmpty() || pass.isNullOrEmpty()) {
            editState.value = EditProfileStates.WAITING
            return
        }
        repository?.editUserInfo(nick, pass)
    }
}