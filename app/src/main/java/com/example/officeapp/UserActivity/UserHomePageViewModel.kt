package com.example.officeapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.collections.ArrayList

class UserHomePageViewModel(val state: SavedStateHandle) : ViewModel(), UserHomePageRepositoryConnector
{
    private val dateList:MutableLiveData<ArrayList<Date>> = state.getLiveData(Constants.DATE_LIST)
        fun getDateList():LiveData<ArrayList<Date>>{return dateList}

    private val boundaries:MutableLiveData<ArrayList<Date>> = state.getLiveData(Constants.SEVEN_DAYS)
        fun getBoundaryList():LiveData<ArrayList<Date>>{return boundaries}

    private val repository=UserHomePageRepository(this)

    init{
        setDates(setCalendarStart(), setCalendarEnd())
        onCapacityChanged()
    }

    override fun onDatesDateChanged(newDates:ArrayList<Date>) {
        for(i in 0 until dateList.value!!.size)
            for(j in 0 until newDates.size)
                if(dateList.value!!.get(i).equals(newDates.get(j)))
                    dateList.value!![i]=newDates.get(j)
        dateList.value=dateList.value
    }

    override fun onCapacityChanged() {
        var dt = Date()
        val boundaryList=ArrayList<Date>()
        for(i in 1..7) {
            val c: Calendar = Calendar.getInstance()
            c.setTime(dt)
            c.add(Calendar.DATE, 1)
            dt = c.getTime()

            val date=Date(dt.date, dt.month, dt.year+1900, repository.getCapacity())
            boundaryList.add(date)
            for(i in 0 until dateList.value!!.size)
                if(dateList.value!!.get(i).equals(date))
                    dateList.value!![i]=date
        }
        boundaries.value=boundaryList
        dateList.value=dateList.value
    }

    private fun setCalendarStart(): Calendar
    {
        val month=Date().month
        val year=Date().year
        val start= Calendar.getInstance()

        start.set(Calendar.MONTH, month)
        start.set(Calendar.YEAR, year+1900)
        start.set(Calendar.DAY_OF_MONTH, 1)
        start.getTime()

        start.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        if (start.get(Calendar.MONTH) <= month)
            start.add(Calendar.DATE, -7)

        return start
    }

    private fun setCalendarEnd(): Calendar
    {
        val month=Date().month
        val year=Date().year
        val end = Calendar.getInstance()

        end.set(Calendar.MONTH, month+1)
        end.set(Calendar.YEAR, year+1900)
        end.set(Calendar.DAY_OF_MONTH, 1)
        end.getTime()
        end.set(Calendar.DATE, -1)
        end.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
        if (end.get(Calendar.MONTH) != month+1)
            end.add(Calendar.DATE, + 7)

        return end
    }

    private fun setDates(start: Calendar, end:Calendar)
    {
        dateList.value= ArrayList()

        var date=Date(start.time.date, start.time.month, start.time.year+1900, 10)
        dateList.value?.add(date)
        while (start.before(end))
        {
            start.add(Calendar.DATE, 1)
            date=Date(start.time.date, start.time.month, start.time.year+1900, 10)
            dateList.value?.add(date)
        }
    }
}