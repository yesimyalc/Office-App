package com.example.officeapp

import android.util.Log
import com.google.firebase.database.*

class MainRepository(val connector:LogInRepVMConnector)
{
    private val employeesRef: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("Employees")

    fun retrieveUser(searchedNick:String, searchedPass:String)
    {
        employeesRef.orderByChild(Constants.R_USER_NICK).equalTo(searchedNick).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(!snapshot.exists())
                    connector.onRetrieveUser(null)
                else
                {
                    for(ds in snapshot.children)
                        if(ds.child(Constants.R_USER_NICK).value==searchedNick && ds.child(Constants.R_USERPASS).value==searchedPass)
                        {
                            val id=ds.key.toString()
                            val nick=ds.child(Constants.R_USER_NICK).value.toString()
                            val name=ds.child(Constants.R_USERNAME).value.toString()
                            val pass=ds.child(Constants.R_USERPASS).value.toString()
                            val status=ds.child(Constants.R_USERSTATUS).value.toString()

                            val user: User?
                            if(status==LogInStates.ADMIN.text)
                                user=Admin(id, nick, name, pass)
                            else
                                user=Employee(id, nick, name, pass)

                            connector.onRetrieveUser(user)
                            break
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ERROR", error.message)
            }
        })
    }
}