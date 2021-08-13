package com.example.officeapp

import android.util.Log
import com.google.firebase.database.*
import java.util.*
import com.google.firebase.FirebaseError

import com.google.firebase.database.DataSnapshot

import com.google.firebase.database.ValueEventListener


class AddUserRepository(val connector: AddUserRepViewModelConnector)
{
    private val userRef: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("Employees")

    fun addUser(nick:String, name:String, pass:String, admin:Boolean)
    {
        userRef.orderByChild("r_user_nick").equalTo(nick).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    connector.setState("User already exists.")
                }
                else
                {
                    val id=UUID.randomUUID().toString()
                    userRef.child(id).child(Constants.R_USER_NICK).setValue(nick)
                    userRef.child(id).child(Constants.R_USERNAME).setValue(name)
                    userRef.child(id).child(Constants.R_USERPASS).setValue(pass)
                    if(admin)
                        userRef.child(id).child(Constants.R_USERSTATUS).setValue("ADMIN")
                    else
                        userRef.child(id).child(Constants.R_USERSTATUS).setValue("EMPLOYEE")

                    connector.setState("New user is added.")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ERROR", error.message)
            }
        })
    }
}