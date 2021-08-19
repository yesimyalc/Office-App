package com.example.officeapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LogInViewModel:ViewModel(),LogInRepVMConnector
{
    private val repository=MainRepository(this)
    private var retrievedUser:User?=null
        fun getRetrievedUser():User?{return retrievedUser}

    private val logInState=MutableLiveData<LogInStates>()
        fun getLogInState():LiveData<LogInStates>{return logInState}

    fun logIn(nick: String, pass:String)
    {
        repository.retrieveUser(nick, pass)
    }

    override fun onRetrieveUser(user: User?) {
        retrievedUser=user
        if(user==null)
            logInState.value=LogInStates.INVALID_USER
        else if(user is Admin)
            logInState.value=LogInStates.ADMIN
        else
            logInState.value=LogInStates.EMPLOYEE
    }
}