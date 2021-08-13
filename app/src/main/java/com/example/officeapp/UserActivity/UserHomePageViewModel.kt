package com.example.officeapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.collections.ArrayList

class UserHomePageViewModel(val state: SavedStateHandle) : ViewModel(), HomePageRepositoryConnector
{
    private val dateList:MutableLiveData<ArrayList<Date>> = state.getLiveData(Constants.DATE_LIST)
        fun getDateList():LiveData<ArrayList<Date>>{return dateList}

    private val repository=UserHomePageRepository(this)

    init{
        dateList.value=ArrayList()
    }

    override fun onDatesDateChanged(newDate:Date) {
        for(i in 0..dateList.value!!.size-1)
            if(dateList.value!!.get(i).equals(newDate))
                dateList.value!![i]=newDate
        dateList.value=dateList.value
    }

    override fun onCapacityChanged() {
        var dt = Date()
        dateList.value= ArrayList()
        for(i in 1..7) {
            val c: Calendar = Calendar.getInstance()
            c.setTime(dt)
            c.add(Calendar.DATE, 1)
            dt = c.getTime()
            dateList.value?.add(Date(dt.date, dt.month, dt.year+1900, repository.getCapacity()))
        }
        dateList.value=dateList.value
    }
}