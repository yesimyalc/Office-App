package com.example.officeapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel: SettingsRepViewModelConnector, ViewModel()
{
    private val capacity= MutableLiveData<Int>()
        fun getCapacity():LiveData<Int>{return capacity}

    private val repository=SettingsRepository(this)

    override fun onCapChanged(newCap: Int) {
        capacity.value=newCap
    }

    fun changeCapacity(newCap: Int)
    {
        repository.changeCapacity(newCap)
    }

}