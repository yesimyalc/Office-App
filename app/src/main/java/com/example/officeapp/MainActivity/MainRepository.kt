package com.example.officeapp

import android.util.Log
import com.google.firebase.database.*

class MainRepository
{
    private val employeesRef: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("Employees")

    private val allUsers=HashMap<String, User>()
        fun getAllUsers():HashMap<String, User>{return allUsers}

    fun retrieveAllUsers()
    {
        employeesRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                /*if(allUsers.size>snapshot.children.count())
                    allUsers.clear()*/

                for(ds in snapshot.children)
                {
                    val id=ds.key.toString()
                    val nick=ds.child(Constants.R_USER_NICK).value.toString()
                    val name=ds.child(Constants.R_USERNAME).value.toString()
                    val pass=ds.child(Constants.R_USERPASS).value.toString()
                    val status=ds.child(Constants.R_USERSTATUS).value.toString()

                    var user:User?=null
                    if(status=="ADMIN")
                        user=Admin(id, nick, name, pass)
                    else
                        user=Employee(id, nick, name, pass)

                    allUsers.put(user.getUserID(), user)


                    /*val index=allUsers.get(user.getUserID())
                    if(index==null)
                        allUsers.put(user.getUserID(), user)
                    else
                    {
                        val oldEmployee=index
                        if(oldEmployee?.getUserPassword()!=pass)
                            oldEmployee?.changePassword(ds.child(Constants.R_USERPASS).value.toString())
                        if(oldEmployee?.getUserNickname()!=nick)
                            oldEmployee?.changeNickName(ds.child(Constants.R_USER_NICK).value.toString())
                        if(oldEmployee?.getUserName()!=name)
                            oldEmployee?.changeUserName(ds.child(Constants.R_USERNAME).value.toString())
                    }*/
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ERROR", error.message)
            }
        })
    }

    fun containsUser(searchedNick:String, searchedPass:String):User?
    {
        for(user in allUsers)
            if(user.value.getUserNickname()==searchedNick && user.value.getUserPassword()==searchedPass)
                return user.value

        return null
    }
}