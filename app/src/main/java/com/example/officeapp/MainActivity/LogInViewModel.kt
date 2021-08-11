package com.example.officeapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class LogInViewModel(val state: SavedStateHandle):ViewModel()
{
    private val repository=MainRepository()
    private val enteredNick:MutableLiveData<String> =state.getLiveData(Constants.ENTERED_NICK)
        fun getEnteredNick():LiveData<String>{return enteredNick}
        fun setEnteredNick(s:String){enteredNick.value=s}
    private val enteredPass:MutableLiveData<String> =state.getLiveData(Constants.ENTERED_PASS)
        fun getEnteredPass():LiveData<String>{return enteredPass}
        fun setEnteredPass(s:String){enteredPass.value=s}

    init{
        enteredNick.value=String()
        enteredPass.value= String()
        repository.retrieveAllUsers()
    }

    fun logIn():User?
    {
        return repository.containsUser(enteredNick.value.toString(), enteredPass.value.toString())
    }
}