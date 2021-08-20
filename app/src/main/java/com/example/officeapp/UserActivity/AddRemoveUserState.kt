package com.example.officeapp

enum class AddRemoveUserState(val text:String) {
    USER_ADDED("New user is added."),
    EXISTING_USER("User already exists."),
    USER_REMOVED("User is removed."),
    NON_EXISTING_USER("User does not exist."),
    WAITING_INFO("Enter user information first."),
    WAITING_STATUS("Select admin or employee."),
    CURRENT_ACCOUNT("Removing current account."),
    REFRESHED("")
}