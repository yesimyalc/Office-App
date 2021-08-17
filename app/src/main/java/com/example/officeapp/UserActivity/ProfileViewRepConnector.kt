package com.example.officeapp

interface ProfileViewRepConnector
{
    fun onRetrieveUser(user:User)
    fun onChangeEditState(editState:String)
}