package com.example.officeapp

import android.util.Log
import com.google.firebase.database.*

class DateRepository(val dateId:String, val connector: DateRepositoryViewModelConnector)
{
    private val dateRef: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("Calendar").child(dateId).child("Users")
    private val userRef: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("Employees")

    init{
        dateRef.addValueEventListener(object : ValueEventListener{
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
    }

    fun addUser(userID:String, userName:String)
    {
        dateRef.child(userID).setValue(userName)
        userRef.child(userID).child("Days").child(dateId).setValue(true)
    }

    fun deleteUser(userID:String)
    {
        dateRef.child(userID).removeValue()
        userRef.child(userID).child("Days").child(dateId).removeValue()
    }
}