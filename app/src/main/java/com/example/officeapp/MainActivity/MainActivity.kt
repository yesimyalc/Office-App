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
        val userPass=Hawk.get<String>(Constants.LOGGEDIN_PASS)
        val userNick=Hawk.get<String>(Constants.LOGGEDIN_USERNICK)

        if(userNick!=null && userPass!=null)
            viewModel.logIn(userNick, userPass)
    }

    private fun startAdminActivity()
    {
        Hawk.put(Constants.LOGGEDIN_PASS, viewModel.getRetrievedUser()!!.getUserPassword())
        Hawk.put(Constants.LOGGEDIN_USERNICK, viewModel.getRetrievedUser()!!.getUserNickname())
        Hawk.put(Constants.LOGGEDIN_USERNAME, viewModel.getRetrievedUser()!!.getUserName())
        Hawk.put(Constants.LOGGEDIN_USERID, viewModel.getRetrievedUser()!!.getUserID())

        Intent(this, AdminActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }.also {
            startActivity(it)
            finish()
        }

    }

    private fun startEmployeeActivity()
    {
        Hawk.put(Constants.LOGGEDIN_PASS, viewModel.getRetrievedUser()!!.getUserPassword())
        Hawk.put(Constants.LOGGEDIN_USERNICK, viewModel.getRetrievedUser()!!.getUserNickname())
        Hawk.put(Constants.LOGGEDIN_USERNAME, viewModel.getRetrievedUser()!!.getUserName())
        Hawk.put(Constants.LOGGEDIN_USERID, viewModel.getRetrievedUser()!!.getUserID())

        val intent= Intent(this, EmployeeActivity::class.java)
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