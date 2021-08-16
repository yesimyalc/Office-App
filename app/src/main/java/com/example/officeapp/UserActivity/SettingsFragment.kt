package com.example.officeapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

class SettingsFragment : Fragment(R.layout.fragment_settings)
{
    private var fragmentView:View?=null
    private val viewModel:SettingsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        fragmentView=super.onCreateView(inflater, container, savedInstanceState)
        var startCap:Int?=null

        val capacityPicker=fragmentView?.findViewById<NumberPicker>(R.id.capacityPicker)
        capacityPicker?.maxValue=1000
        capacityPicker?.minValue=0

        viewModel.getCapacity().observe(viewLifecycleOwner, {
            startCap=viewModel.getCapacity().value!!
            capacityPicker?.value=viewModel.getCapacity().value!!
        })

        val saveSettingsButton=fragmentView?.findViewById<LinearLayout>(R.id.saveSettingsButton)
        saveSettingsButton?.setOnClickListener{
            if(capacityPicker?.value!!!=startCap)
                viewModel.changeCapacity(capacityPicker?.value!!)
        }

        return fragmentView
    }
}