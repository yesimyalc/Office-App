package com.example.officeapp

interface DateRepositoryViewModelConnector
{
    fun onChangeDayUsers(users:HashMap<String, String>)
    fun onCapacityChange(newCap:Long)
}