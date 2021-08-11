package com.example.officeapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class DateViewModel(val state: SavedStateHandle) : ViewModel()
{
    private val chosenDate:MutableLiveData<Date> =state.getLiveData(Constants.CHOSEN_DATE)
        fun getChosenDate():LiveData<Date>{return chosenDate}

    val loggedInUserID:MutableLiveData<String> =state.getLiveData<String>(Constants.LOGGEDIN_USERID)
    val loggedInUserName:MutableLiveData<String> =state.getLiveData<String>(Constants.LOGGEDIN_USERNAME)

    private var repository:DateRepository?=null

    fun addUser()
    {
        chosenDate.value?.addUser(loggedInUserID.value!!, loggedInUserName.value!!)
        chosenDate.value=chosenDate.value
        repository?.addUser()
    }

    fun deleteUser()
    {
        chosenDate.value?.deleteUser(loggedInUserID.value!!)
        chosenDate.value=chosenDate.value
        repository?.deleteUser()
    }

    fun setChosenDate(date:Date)
    {
        chosenDate.value=date
    }

    fun setLoggedInUser(userName:String, userID:String)
    {
        loggedInUserID.value=userID
        loggedInUserName.value=userName
        repository=DateRepository(""+chosenDate.value!!.date+" "+chosenDate.value!!.getMonthNoString()+" "+chosenDate.value!!.year, loggedInUserID.value!!, loggedInUserName.value!!)
    }
}