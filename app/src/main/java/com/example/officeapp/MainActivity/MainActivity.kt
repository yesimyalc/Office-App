package com.example.officeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels

class MainActivity : AppCompatActivity()
{
    private val viewModel:LogInViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val enteredNick=findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.enteredNick)
        val enteredPass=findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.enteredPass)

        enteredNick.text.toString()
        enteredNick.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setEnteredNick(s.toString())
            }
            override fun afterTextChanged(s: Editable?){}
        })

        enteredPass.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setEnteredPass(s.toString())
            }
            override fun afterTextChanged(s: Editable?){}
        })

        if(enteredNick.text.isNullOrEmpty() && viewModel.getEnteredNick().value!!.isNotEmpty())
            enteredNick.setText(viewModel.getEnteredNick().value!!)

        if(enteredNick.text.isNullOrEmpty() && viewModel.getEnteredPass().value!!.isNotEmpty())
            enteredPass.setText(viewModel.getEnteredPass().value!!)
    }

    fun onEnterLogIn(view: View)
    {
        val user=viewModel.logIn()
        if(user==null)
            Toast.makeText(this, "Wrong user nick or password. Please try again.", Toast.LENGTH_LONG).show()
        else if(user is Employee)
        {
            val intent= Intent(this, EmployeeActivity::class.java)
            intent.putExtra(Constants.LOGGEDIN_USERID, user.getUserID())
            intent.putExtra(Constants.LOGGEDIN_USERNAME, user.getUserName())
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent)
            finish()
        }
        else
        {
            val intent= Intent(this, AdminActivity::class.java)
            intent.putExtra(Constants.LOGGEDIN_USERID, user.getUserID())
            intent.putExtra(Constants.LOGGEDIN_USERNAME, user.getUserName())
            intent.putExtra(Constants.LOGGEDIN_USERNICK, user.getUserNickname())
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent)
            finish()
        }
    }
}