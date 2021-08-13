package com.example.officeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView

class AdminActivity : AppCompatActivity(),ActivityFragmentConnector
{
    private val homeFragment= UserHomePageFragment.newInstance(this)
    private val profileFragment= UserProfileFragment()
    private val dateFragment= DateFragment()
    private val addUserFragment=AddUserFragment()
    private val settingsFragment=SettingsFragment()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin2)

        val bundle = Bundle()
        bundle.putString(Constants.LOGGEDIN_USERID, intent.getStringExtra(Constants.LOGGEDIN_USERID))
        bundle.putString(Constants.LOGGEDIN_USERNAME, intent.getStringExtra(Constants.LOGGEDIN_USERNAME))
        profileFragment.arguments = bundle

        changeFragment(homeFragment)

        val navigationBarAdmin=findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.navigationBarAdmin)
        navigationBarAdmin.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when(item.itemId)
                {
                    R.id.home ->{
                        if(dateFragment.isActive==false)
                            changeFragment(homeFragment)
                        else
                            changeFragment(dateFragment)
                    }
                    R.id.profile ->{changeFragment(profileFragment)}
                    R.id.addUser ->{changeFragment(addUserFragment)}
                    R.id.settings ->{changeFragment(settingsFragment)}
                }
                return true
            }
        })
    }

    fun changeFragment(fragment: Fragment)
    {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.currentPage, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onShowDateInfo(date: Date) {
        val bundle = Bundle()
        bundle.putSerializable(Constants.CHOSEN_DATE, date)
        bundle.putString(Constants.LOGGEDIN_USERID, intent.getStringExtra(Constants.LOGGEDIN_USERID))
        bundle.putString(Constants.LOGGEDIN_USERNAME, intent.getStringExtra(Constants.LOGGEDIN_USERNAME))
        dateFragment.arguments = bundle
        dateFragment.isActive=true
        changeFragment(dateFragment)
    }
}