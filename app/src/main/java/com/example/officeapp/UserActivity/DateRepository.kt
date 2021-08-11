package com.example.officeapp

import android.util.Log
import com.google.firebase.database.*

class DateRepository(val dateId:String, val userID:String, val userName:String)
{
    private val dateRef: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("Calendar").child(dateId).child("Users")
    private val userRef: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("Employees").child(userID)

    fun addUser()
    {
        dateRef.child(userID).setValue(userName)
        userRef.child("Days").child(dateId).setValue(true)
    }

    fun deleteUser()
    {
        dateRef.child(userID).removeValue()
        userRef.child("Days").child(dateId).removeValue()
    }
}