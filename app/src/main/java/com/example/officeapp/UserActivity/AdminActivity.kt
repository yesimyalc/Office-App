package com.example.officeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.navigation.NavigationBarView

class AdminActivity : AppCompatActivity(),ActivityFragmentConnector
{
    private val homeFragment= UserHomePageFragment.newInstance(this)
    private val profileFragment= UserProfileFragment()
    private val dateFragment= DateFragment(R.layout.fragment_date_admin)
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

        val nickBundle=Bundle()
        nickBundle.putString(Constants.LOGGEDIN_USERNICK, intent.getStringExtra(Constants.LOGGEDIN_USERNICK))
        addUserFragment.arguments = nickBundle

        changeFragment(homeFragment, "Home Page")

        val navigationBarAdmin=findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.navigationBarAdmin)
        navigationBarAdmin.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when(item.itemId)
                {
                    R.id.home ->{ changeFragment(homeFragment, "Home Page") }
                    R.id.profile ->{changeFragment(profileFragment, "Profile Page")}
                    R.id.addUser ->{changeFragment(addUserFragment, "Add User Page")}
                    R.id.settings ->{changeFragment(settingsFragment, "Settings Page")}
                }
                return true
            }
        })

        supportFragmentManager.addOnBackStackChangedListener {

            val dateFragment: DateFragment? = supportFragmentManager.findFragmentByTag("Date Page") as DateFragment?
            val homeFragment: UserHomePageFragment? = supportFragmentManager.findFragmentByTag("Home Page") as UserHomePageFragment?
            if (((dateFragment != null && dateFragment.isVisible()) || (homeFragment != null && homeFragment.isVisible())) && navigationBarAdmin.selectedItemId!=R.id.home) {
                navigationBarAdmin?.menu?.getItem(0)?.setChecked(true)
            }
        }
    }

    fun changeFragment(fragment: Fragment, tag:String)
    {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.currentPage, fragment, tag)

        val myFragment: UserHomePageFragment? = supportFragmentManager.findFragmentByTag("Home Page") as UserHomePageFragment?
        val myFragment2: DateFragment? = supportFragmentManager.findFragmentByTag("Date Page") as DateFragment?

        if (myFragment != null && myFragment.isVisible())
            fragmentTransaction.addToBackStack("Home Page")
        else if (myFragment2 != null && myFragment2.isVisible())
            fragmentTransaction.addToBackStack("Date Page")

        fragmentTransaction.commit()
    }

    override fun onShowDateInfo(date: Date) {
        val bundle = Bundle()
        bundle.putSerializable(Constants.CHOSEN_DATE, date)
        bundle.putString(Constants.LOGGEDIN_USERID, intent.getStringExtra(Constants.LOGGEDIN_USERID))
        bundle.putString(Constants.LOGGEDIN_USERNAME, intent.getStringExtra(Constants.LOGGEDIN_USERNAME))
        dateFragment.arguments = bundle
        changeFragment(dateFragment, "Date Page")
    }
}