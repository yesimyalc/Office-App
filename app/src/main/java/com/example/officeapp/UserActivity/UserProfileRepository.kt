package com.example.officeapp

import android.util.Log
import com.google.firebase.database.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UserProfileRepository(val userID: String, connector: ProfileViewRepConnector)
{
    private val userRef: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("Employees").child(userID)
    private val dateRef: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("Calendar")

    init{
        userRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val nick=snapshot.child(Constants.R_USER_NICK).value.toString()
                val name=snapshot.child(Constants.R_USERNAME).value.toString()
                val pass=snapshot.child(Constants.R_USERPASS).value.toString()
                val status=snapshot.child(Constants.R_USERSTATUS).value.toString()
                val days=ArrayList<String>()

                for(ds in snapshot.child("Days").children)
                    days.add(ds.key!!)

                var user:User?=null
                if(status=="ADMIN")
                    user=Admin(userID, nick, name, pass)
                else
                    user=Employee(userID, nick, name, pass)

                val format: DateFormat = SimpleDateFormat("dd MM yyyy", Locale.ENGLISH)
                val c: Calendar = Calendar.getInstance()
                for(day in days)
                {
                    val date:java.util.Date?=format.parse(day)
                    if(date?.compareTo(c.time)==0 || date?.compareTo(c.time)==-1)
                        userRef.child("Days").child(day).removeValue()
                    else
                        user.addDay(day)
                }

                connector.onRetrieveUser(user)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ERROR", error.message)
            }
        })
    }

    fun deleteUserFromDate(day:String)
    {
        dateRef.child(day).child("Users").child(userID).removeValue()
        userRef.child("Days").child(day).removeValue()
    }
}