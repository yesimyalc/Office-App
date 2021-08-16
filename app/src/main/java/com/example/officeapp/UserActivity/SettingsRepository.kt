package com.example.officeapp

import android.util.Log
import com.google.firebase.database.*

class SettingsRepository(val connector: SettingsRepViewModelConnector)
{
    private val capacityRef: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("General Capacity")

    init{
        capacityRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                connector.onCapChanged(snapshot.value.toString().toInt())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ERROR", error.message)
            }
        })
    }

    fun changeCapacity(newCap:Int)
    {
        capacityRef.setValue(newCap)
    }
}