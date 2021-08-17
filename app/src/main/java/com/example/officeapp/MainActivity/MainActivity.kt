package com.example.officeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.orhanobut.hawk.Hawk

class MainActivity : AppCompatActivity()
{
    private val viewModel:LogInViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Hawk.init(this).build()
        initiateAutoLogIn()

        viewModel.getLogInState().observe(this, {
            if(viewModel.getLogInState().value==LogInStates.INVALID_USER)
                Toast.makeText(this, LogInStates.INVALID_USER.text, Toast.LENGTH_SHORT).show()
            else if(viewModel.getLogInState().value==LogInStates.EMPLOYEE)
                startEmployeeActivity()
            else
                startAdminActivity()
        })
    }

    private fun initiateAutoLogIn()
    {
        val userPass=Hawk.get<String>("loggedInUserPass")
        val userNick=Hawk.get<String>("loggedInUserNick")

        if(userNick!=null && userPass!=null)
            viewModel.logIn(userNick, userPass)
    }

    private fun startAdminActivity()
    {
        Hawk.put("loggedInUserPass", viewModel.getRetrievedUser()!!.getUserPassword())
        Hawk.put("loggedInUserNick", viewModel.getRetrievedUser()!!.getUserNickname())

        Intent(this, AdminActivity::class.java).apply {
            putExtra(Constants.LOGGEDIN_USERID, viewModel.getRetrievedUser()!!.getUserID())
            putExtra(Constants.LOGGEDIN_USERNAME, viewModel.getRetrievedUser()!!.getUserName())
            putExtra(Constants.LOGGEDIN_USERNICK, viewModel.getRetrievedUser()!!.getUserNickname())
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }.also {
            startActivity(it)
            finish()
        }

    }

    private fun startEmployeeActivity()
    {
        Hawk.put("loggedInUserPass", viewModel.getRetrievedUser()!!.getUserPassword())
        Hawk.put("loggedInUserNick", viewModel.getRetrievedUser()!!.getUserNickname())

        val intent= Intent(this, EmployeeActivity::class.java)
        intent.putExtra(Constants.LOGGEDIN_USERID, viewModel.getRetrievedUser()!!.getUserID())
        intent.putExtra(Constants.LOGGEDIN_USERNAME, viewModel.getRetrievedUser()!!.getUserName())
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent)
        finish()
    }

    fun onEnterLogIn(view: View)
    {
        val enteredNick=findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.enteredNick)
        val enteredPass=findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.enteredPass)
        viewModel.logIn(enteredNick.text.toString(), enteredPass.text.toString())
    }
}