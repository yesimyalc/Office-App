package com.example.officeapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class SettingsFragment : Fragment(R.layout.fragment_settings)
{
    private var fragmentView:View?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        fragmentView=super.onCreateView(inflater, container, savedInstanceState)



        return fragmentView
    }
}