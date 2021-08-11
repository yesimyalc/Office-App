package com.example.officeapp

import java.io.Serializable
import java.lang.IllegalArgumentException
import java.util.Date

class Date (date:Int, month:Int, year:Int, max:Int, peopleAmount:Int=0):Date(year, month, date),Serializable
{
    private var state="Mostly Available"
        fun getState():String{return state}
    private var maxCapacity:Int=max
        fun getCap():Int{return maxCapacity}
    private var current:Int=peopleAmount
        fun getCurrentPeopleAmount():Int{return current}
    private var monthNoString:String?=null
         fun getMonthNoString():String?{return monthNoString}
    private var monthString:String?=null
        fun getMonthString():String?{return monthString}
    private var weekDayString:String?=null
        fun getWeekDayString():String?{return weekDayString}

    private var users=HashMap<String, String>()
        fun getUsers():HashMap<String, String>{return users}

    init
    {
        setMonthString()
        setWeekDayString()
        setMonthNoString()
        setCapacity(max)
        setPeopleAmount(peopleAmount)
    }

    fun setMonthNoString()
    {
        if((month+1)<10)
            monthNoString="0"+(month+1).toString()
        else
            monthNoString=(month+1).toString()
    }

    fun setWeekDayString()
    {
        when(day)
        {
            2->weekDayString="Monday"
            3->weekDayString="Tuesday"
            4->weekDayString="Wednesday"
            5->weekDayString="Thursday"
            6->weekDayString="Friday"
            0->weekDayString="Saturday"
            1->weekDayString="Sunday"
        }
    }

    fun setMonthString()
    {
        when(month)
        {
            0->monthString="January"
            1->monthString="February"
            2->monthString="March"
            3->monthString="April"
            4->monthString="May"
            5->monthString="June"
            6->monthString="July"
            7->monthString="August"
            8->monthString="September"
            9->monthString="October"
            10->monthString="November"
            11->monthString="December"
        }
    }

    fun setCapacity(newCap:Int)
    {
        if(newCap>=0 && newCap>=current)
            maxCapacity=newCap
        else
            throw IllegalArgumentException("You cannot assign $newCap as maxCapacity. It is too low")

        setState()
    }

    fun setPeopleAmount(newAmount:Int)
    {
        if(newAmount<0 || newAmount>maxCapacity)
            throw IllegalArgumentException("You cannot assign $newAmount as people amount.")
        else
            current=newAmount
    }

    fun setState()
    {
        if(current<=maxCapacity/2)
            state="Mostly Available"
        else if(current>maxCapacity/2 && current<maxCapacity)
            state="Mostly Full"
        else
            state="Full"
    }

    fun addUser(userID:String, userName:String)
    {
        users.put(userID, userName)
        current++
        setState()
    }

    fun deleteUser(userID:String)
    {
        users.remove(userID)
        current--
        setState()
    }

    override fun equals(other: Any?): Boolean {
        if(other !is Date || other==null)
            return false
        else if(date==other.date && month==other.month && year==other.year)
            return true
        else
            return false
    }

    fun toStringText(): String {
        return ""+date+" "+getMonthString()+" "+year
    }

    fun isParticipant(userID:String):Boolean
    {
        if(users.contains(userID))
            return true

        return false
    }
}