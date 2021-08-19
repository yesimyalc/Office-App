package com.example.officeapp

enum class EditProfileStates(val text: String){
    SUCCESSFUL("New information has been saved."),
    NOT_SUCCESSFUL("There is already a user with the given nickname."),
    WAITING("Enter information first."),
    REFRESHED("")
}