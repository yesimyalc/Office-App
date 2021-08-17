package com.example.officeapp

import java.io.Serializable

interface User:Serializable
{
    fun getUserID():String
    fun getUserNickname():String
    fun getUserName():String
    fun getUserPassword():String
    fun getParticipatedDays():ArrayList<String>

    fun addDay(day:String)
    fun deleteDay(day:String)
}