package com.example.officeapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class DateViewModel(val state: SavedStateHandle) : ViewModel(), DateRepositoryViewModelConnector
{
    private val chosenDate:MutableLiveData<Date> =state.getLiveData(Constants.CHOSEN_DATE)
        fun getChosenDate():LiveData<Date>{return chosenDate}

    private var repository:DateRepository?=null

    fun addUser(userID:String, userName:String)
    {
        chosenDate.value=chosenDate.value
        repository?.addUser(userID, userName, chosenDate?.value!!.getCap())
    }

    fun deleteUser(userID: String)
    {
        chosenDate.value=chosenDate.value
        repository?.deleteUser(userID)
    }

    fun setChosenDate(date:Date)
    {
        chosenDate.value=date
        repository= DateRepository(""+chosenDate.value!!.date+" "+chosenDate.value!!.getMonthNoString()+" "+chosenDate.value!!.year, this)
    }

    fun changeCapacity(newCap: Int)
    {
        repository?.changeCapacity(newCap)
    }

    override fun onChangeDayUsers(users:HashMap<String, String>) {
        chosenDate.value?.getUsers()?.clear()
        chosenDate.value?.getUsers()?.putAll(users)
        chosenDate.value?.setState()
        chosenDate.value=chosenDate.value
    }

    override fun onCapacityChange(newCap: Long) {
        chosenDate.value?.setCapacity(newCap.toInt())
        chosenDate.value=chosenDate.value
    }
}