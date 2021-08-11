package com.example.officeapp

abstract class AbstractUser(id:String, nickname:String, name:String, password:String):User
{
    private val userID:String=id
        override fun getUserID():String{return userID}
    private var userNickname:String=nickname
        override fun getUserNickname():String{return userNickname}
    private var userName:String=name
        override fun getUserName():String{return userName}
    private var userPassword:String=password
        override fun getUserPassword():String{return userPassword}

    private var participatedDays=ArrayList<String>()
        override fun getParticipatedDays():ArrayList<String>{return participatedDays}

    override fun changeNickName(newNickName: String) {
        TODO("Not yet implemented")
    }

    override fun changePassword(newPassword: String) {

    }

    override fun changeUserName(newUserName: String) {
        TODO("Not yet implemented")
    }

    override fun equals(other: Any?): Boolean {
        if(other !is User || other==null)
            return false
        else
            return userID.equals(other.getUserID())
    }

    override fun addDay(day:String)
    {
        participatedDays.add(day)
    }

    override fun deleteDay(day:String)
    {
        participatedDays.remove(day)
    }
}