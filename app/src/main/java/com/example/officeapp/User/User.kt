package com.example.officeapp

import java.io.Serializable

interface User:Serializable
{
    fun getUserID():String
    fun getUserNickname():String
    fun getUserName():String
    fun getUserPassword():String
    fun getParticipatedDays():ArrayList<String>

    fun changeNickName(newNickName:String)
    fun changePassword(newPassword:String)
    fun changeUserName(newUserName:String)

    fun addDay(day:String)
    fun deleteDay(day:String)
}