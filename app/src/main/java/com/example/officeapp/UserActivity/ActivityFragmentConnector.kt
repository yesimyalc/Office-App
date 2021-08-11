package com.example.officeapp

import java.io.Serializable

interface ActivityFragmentConnector:Serializable {
    fun onShowDateInfo(date:Date)
}