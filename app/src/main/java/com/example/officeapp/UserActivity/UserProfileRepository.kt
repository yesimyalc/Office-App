package com.example.officeapp

import android.util.Log
import com.google.firebase.database.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UserProfileRepository(val userID: String, val connector: ProfileViewRepConnector)
{
    private val userRef: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("Employees")
    private val dateRef: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("Calendar")

    init{
        userRef.child(userID).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val nick=snapshot.child(Constants.R_USER_NICK).value.toString()
                val name=snapshot.child(Constants.R_USERNAME).value.toString()
                val pass=snapshot.child(Constants.R_USERPASS).value.toString()
                val status=snapshot.child(Constants.R_USERSTATUS).value.toString()
                val days=ArrayList<String>()

                for(ds in snapshot.child("Days").children)
                    days.add(ds.key!!)

                var user:User?=null
                if(status==LogInStates.ADMIN.text)
                    user=Admin(userID, nick, name, pass)
                else
                    user=Employee(userID, nick, name, pass)

                val format: DateFormat = SimpleDateFormat("dd MM yyyy", Locale.ENGLISH)
                val c: Calendar = Calendar.getInstance()
                for(day in days)
                {
                    val date:java.util.Date?=format.parse(day)
                    if(date?.compareTo(c.time)==0 || date?.compareTo(c.time)==-1)
                        userRef.child(userID).child("Days").child(day).removeValue()
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
        userRef.child(userID).child("Days").child(day).removeValue()
    }

    fun editUserInfo(nick: String, pass:String)
    {
        userRef.orderByChild(Constants.R_USER_NICK).equalTo(nick).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(!snapshot.exists()) {
                    userRef.child(userID).child(Constants.R_USER_NICK).setValue(nick)
                    connector.onChangeEditState(EditProfileStates.SUCCESSFUL)
                }
                else
                {
                    for(ds in snapshot.children)
                        if(ds.child(Constants.R_USER_NICK).value==nick)
                        {
                            if(ds.key!=userID)
                                connector.onChangeEditState(EditProfileStates.NOT_SUCCESSFUL)
                            else
                                connector.onChangeEditState(EditProfileStates.SUCCESSFUL)

                            break
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ERROR", error.message)
            }
        })
        userRef.child(userID).child(Constants.R_USERPASS).setValue(pass)
    }
}