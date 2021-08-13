package com.example.officeapp

import android.util.Log
import com.google.firebase.database.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class UserHomePageRepository(val connector: HomePageRepositoryConnector)
{
    private val calendarRef:DatabaseReference = FirebaseDatabase.getInstance().getReference().child("Calendar")
    private val capacityRef: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("General Capacity")

    private val calendar=ArrayList<Date>()
        fun getCalendar():ArrayList<Date>{return calendar}

    private var generalCapacity:Int=10
        fun getCapacity():Int{return generalCapacity}

    init{
        capacityRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                generalCapacity=snapshot.value.toString().toInt()
                connector.onCapacityChanged()
                retrieveCalendar()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ERROR", error.message)
            }
        })
    }

    fun retrieveCalendar()
    {
        calendarRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val format: DateFormat = SimpleDateFormat("dd MM yyyy", Locale.ENGLISH)
                val c: Calendar = Calendar.getInstance()
                for(ds in snapshot.children)
                {
                    val date:java.util.Date?=format.parse(ds.key.toString())
                    if(date?.compareTo(c.time)==0 || date?.compareTo(c.time)==-1)
                        ds.ref.removeValue()
                    else
                    {
                        var max:Int?=null
                        if(ds.hasChild("Capacity"))
                            max=ds.child("Capacity").value.toString().toInt()
                        val addedDate=Date(date?.date!!, date.month, (date.year)+1900, max?:10)
                        for(user in ds.child("Users").children) {
                            addedDate.addUser(user.key!!, user.value!!.toString())
                        }
                        calendar.add(addedDate)
                        connector.onDatesDateChanged(addedDate)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ERROR", error.message)
            }
        })
    }
}