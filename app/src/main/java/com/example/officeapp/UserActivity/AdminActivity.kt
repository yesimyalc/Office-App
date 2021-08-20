package com.example.officeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.orhanobut.hawk.Hawk

class AdminActivity : AppCompatActivity(),ActivityFragmentConnector
{
    private val homeFragment= UserHomePageFragment.newInstance(this)
    private val profileFragment= UserProfileFragment()
    private val dateFragment= DateFragment()
    private val addUserFragment=AddUserFragment()
    private val settingsFragment=SettingsFragment()

    private var navigationBarAdmin:BottomNavigationView?=null

    private val viewModel: UserActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin2)

        changeFragment(homeFragment, NavigationStates.HOME_PAGE.stateText)

        navigationBarAdmin=findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.navigationBarAdmin)
        navigationBarAdmin?.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when(item.itemId)
                {
                    R.id.home ->{
                        navigationBarAdmin?.menu?.getItem(0)?.setChecked(true)
                        viewModel.setNavigationState(NavigationStates.HOME_PAGE)
                    }
                    R.id.profile ->{viewModel.setNavigationState(NavigationStates.USER_PROFILE)}
                    R.id.addUser ->{viewModel.setNavigationState(NavigationStates.ADD_USER)}
                    R.id.settings ->{viewModel.setNavigationState(NavigationStates.SETTINGS)}
                }
                return true
            }
        })

        viewModel.getNavigationstate().observe(this, {
            onNavigationStateChanged()
        })

        supportFragmentManager.addOnBackStackChangedListener {
            onReturnedPrevious()
        }
    }

    private fun onNavigationStateChanged()
    {
        when(viewModel.getNavigationstate().value)
        {
            NavigationStates.HOME_PAGE->{
                if(!navigationBarAdmin?.menu?.getItem(0)?.isChecked()!!) {
                    navigationBarAdmin?.menu?.getItem(0)?.setChecked(true)
                }
                else
                    changeFragment(homeFragment, NavigationStates.HOME_PAGE.stateText)
            }
            NavigationStates.DATE_PAGE->{
                if(!navigationBarAdmin?.menu?.getItem(0)?.isChecked!!)
                    navigationBarAdmin?.menu?.getItem(0)?.setChecked(true)
                else
                    changeFragment(dateFragment, NavigationStates.DATE_PAGE.stateText)
            }
            NavigationStates.USER_PROFILE->{
                navigationBarAdmin?.menu?.getItem(1)?.setChecked(true)
                changeFragment(profileFragment, NavigationStates.USER_PROFILE.stateText)
            }
            NavigationStates.ADD_USER->{
                navigationBarAdmin?.menu?.getItem(2)?.setChecked(true)
                changeFragment(addUserFragment, NavigationStates.ADD_USER.stateText)
            }
            NavigationStates.SETTINGS->{
                navigationBarAdmin?.menu?.getItem(3)?.setChecked(true)
                changeFragment(settingsFragment, NavigationStates.SETTINGS.stateText)
            }
        }
    }

    private fun onReturnedPrevious()
    {
        val dateFragment: DateFragment? = supportFragmentManager.findFragmentByTag(NavigationStates.DATE_PAGE.stateText) as DateFragment?
        val homeFragment: UserHomePageFragment? = supportFragmentManager.findFragmentByTag(NavigationStates.HOME_PAGE.stateText) as UserHomePageFragment?
        if (dateFragment != null && dateFragment.isVisible() && navigationBarAdmin?.selectedItemId!=R.id.home) {
            viewModel.setNavigationState(NavigationStates.DATE_PAGE)
        }
        else if(homeFragment != null && homeFragment.isVisible() && navigationBarAdmin?.selectedItemId!=R.id.home){
            viewModel.setNavigationState(NavigationStates.HOME_PAGE)
        }
    }

    private fun changeFragment(fragment: Fragment, tag:String)
    {
        var homeFragment: UserHomePageFragment? = supportFragmentManager.findFragmentByTag(NavigationStates.HOME_PAGE.stateText) as UserHomePageFragment?
        if(homeFragment!=null && fragment is UserHomePageFragment) {
            supportFragmentManager.popBackStackImmediate(NavigationStates.HOME_PAGE.stateText, POP_BACK_STACK_INCLUSIVE)
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.currentPage, fragment, tag)
            fragmentTransaction.commit()
            return
        }

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.currentPage, fragment, tag)

        homeFragment= supportFragmentManager.findFragmentByTag(NavigationStates.HOME_PAGE.stateText) as UserHomePageFragment?
        val dateFragment: DateFragment? = supportFragmentManager.findFragmentByTag(NavigationStates.DATE_PAGE.stateText) as DateFragment?

        if (homeFragment != null && homeFragment.isVisible())
            fragmentTransaction.addToBackStack(NavigationStates.HOME_PAGE.stateText)

        else if (dateFragment != null && dateFragment.isVisible())
            fragmentTransaction.addToBackStack(NavigationStates.DATE_PAGE.stateText)

        fragmentTransaction.commit()
    }

    override fun onShowDateInfo(date: Date) {
        val bundle = Bundle()
        bundle.putSerializable(Constants.CHOSEN_DATE, date)
        Hawk.put(Constants.DATE_LAYOUT, R.layout.fragment_date_admin)
        dateFragment.arguments = bundle
        viewModel.setNavigationState(NavigationStates.DATE_PAGE)
    }
}