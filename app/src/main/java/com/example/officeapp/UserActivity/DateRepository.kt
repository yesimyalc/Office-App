package com.example.officeapp

import android.util.Log
import com.google.firebase.database.*

class DateRepository(val dateId:String, val connector: DateRepositoryViewModelConnector)
{
    private val dateRef: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("Calendar").child(dateId)
    private val userRef: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("Employees")
    private val capacityRef: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("General Capacity")

    private var customCapacity:Long?=null

    init{
        dateRef.child("Users").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList=HashMap<String, String>()
                for(ds in snapshot.children)
                    userList.put(ds.key!!, ds.value!!.toString())

                connector.onChangeDayUsers(userList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ERROR", error.message)
            }
        })

        dateRef.child("Capacity").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value!=null) {
                    connector.onCapacityChange(snapshot.value as Long)
                    customCapacity = snapshot.value as Long
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ERROR", error.message)
            }
        })

        capacityRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(customCapacity==null)
                    connector.onCapacityChange(snapshot.value as Long)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ERROR", error.message)
            }

        })
    }

    fun addUser(userID:String, userName:String, capacity:Int)
    {
        dateRef.child("Users").child(userID).setValue(userName)
        userRef.child(userID).child("Days").child(dateId).setValue(true)
    }

    fun deleteUser(userID:String)
    {
        dateRef.child("Users").child(userID).removeValue()
        userRef.child(userID).child("Days").child(dateId).removeValue()
    }

    fun changeCapacity(newCap:Int)
    {
        dateRef.child("Capacity").setValue(newCap)
    }
}