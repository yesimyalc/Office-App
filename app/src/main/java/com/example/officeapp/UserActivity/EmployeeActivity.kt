package com.example.officeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.tabs.TabLayout

class EmployeeActivity : AppCompatActivity(),ActivityFragmentConnector
{
    private val homeFragment=UserHomePageFragment(this)
    private val profileFragment=UserProfileFragment()
    private val dateFragment=DateFragment()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee)

        val bundle = Bundle()
        bundle.putString(Constants.LOGGEDIN_USERID, intent.getStringExtra(Constants.LOGGEDIN_USERID))
        bundle.putString(Constants.LOGGEDIN_USERNAME, intent.getStringExtra(Constants.LOGGEDIN_USERNAME))
        profileFragment.arguments = bundle

        changeFragment(homeFragment)

        val navigationBarEmployee=findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.navigationBarEmployee)
        navigationBarEmployee.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when(item.itemId)
                {
                    R.id.home->{
                        if(dateFragment.isActive==false)
                            changeFragment(homeFragment)
                        else
                            changeFragment(dateFragment)
                    }
                    R.id.profile->{changeFragment(profileFragment)}
                }
                return true
            }
        })
    }

    fun changeFragment(fragment:Fragment)
    {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.currentPage, fragment)
        fragmentTransaction.commit()
    }

    override fun onShowDateInfo(date:Date) {
        val bundle = Bundle()
        bundle.putSerializable(Constants.CHOSEN_DATE, date)
        bundle.putString(Constants.LOGGEDIN_USERID, intent.getStringExtra(Constants.LOGGEDIN_USERID))
        bundle.putString(Constants.LOGGEDIN_USERNAME, intent.getStringExtra(Constants.LOGGEDIN_USERNAME))
        dateFragment.arguments = bundle
        dateFragment.isActive=true
        changeFragment(dateFragment)
    }

}