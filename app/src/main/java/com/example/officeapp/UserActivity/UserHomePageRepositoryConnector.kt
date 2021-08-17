package com.example.officeapp

interface UserHomePageRepositoryConnector
{
    fun onDatesDateChanged(newDates:ArrayList<Date>)
    fun onCapacityChanged()
}