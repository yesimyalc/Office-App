package com.example.officeapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class UserActivityViewModel(state: SavedStateHandle):ViewModel()
{
    private val navigationState:MutableLiveData<NavigationStates> =state.getLiveData(Constants.NAVIGATION)
        fun getNavigationstate():LiveData<NavigationStates>{return navigationState}
        fun setNavigationState(newState:NavigationStates){
            navigationState.value=newState
        }
}