package com.example.officeapp
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.officeapp.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class EmployeeActivity : AppCompatActivity(), ActivityFragmentConnector
{
    private val homeFragment= UserHomePageFragment.newInstance(this)
    private val profileFragment= UserProfileFragment()
    private val dateFragment= DateFragment()

    private var navigationBarEmployee:BottomNavigationView?=null

    private val viewModel: UserActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee)

        changeFragment(homeFragment, NavigationStates.HOME_PAGE.stateText)

        navigationBarEmployee=findViewById(
            R.id.navigationBarEmployee)
        navigationBarEmployee?.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when(item.itemId)
                {
                    R.id.home ->{
                        navigationBarEmployee?.menu?.getItem(0)?.setChecked(true)
                        viewModel.setNavigationState(NavigationStates.HOME_PAGE)
                    }
                    R.id.profile ->{viewModel.setNavigationState(NavigationStates.USER_PROFILE)}
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

    private fun onNavigationStateChanged() {
        when (viewModel.getNavigationstate().value) {
            NavigationStates.HOME_PAGE -> {
                if (!navigationBarEmployee?.menu?.getItem(0)?.isChecked()!!) {
                    navigationBarEmployee?.menu?.getItem(0)?.setChecked(true)
                } else
                    changeFragment(homeFragment, NavigationStates.HOME_PAGE.stateText)
            }
            NavigationStates.DATE_PAGE -> {
                if (!navigationBarEmployee?.menu?.getItem(0)?.isChecked!!)
                    navigationBarEmployee?.menu?.getItem(0)?.setChecked(true)
                else
                    changeFragment(dateFragment, NavigationStates.DATE_PAGE.stateText)
            }
            NavigationStates.USER_PROFILE -> {
                navigationBarEmployee?.menu?.getItem(1)?.setChecked(true)
                changeFragment(profileFragment, NavigationStates.USER_PROFILE.stateText)
            }
        }
    }

    private fun onReturnedPrevious() {
        val dateFragment: DateFragment? =
            supportFragmentManager.findFragmentByTag(NavigationStates.DATE_PAGE.stateText) as DateFragment?
        val homeFragment: UserHomePageFragment? =
            supportFragmentManager.findFragmentByTag(NavigationStates.HOME_PAGE.stateText) as UserHomePageFragment?
        if (dateFragment != null && dateFragment.isVisible() && navigationBarEmployee?.selectedItemId != R.id.home) {
            viewModel.setNavigationState(NavigationStates.DATE_PAGE)
        } else if (homeFragment != null && homeFragment.isVisible() && navigationBarEmployee?.selectedItemId != R.id.home) {
            viewModel.setNavigationState(NavigationStates.HOME_PAGE)
        }
    }

    private fun changeFragment(fragment: Fragment, tag:String)
    {
        var homeFragment: UserHomePageFragment? = supportFragmentManager.findFragmentByTag(NavigationStates.HOME_PAGE.stateText) as UserHomePageFragment?
        if(homeFragment!=null && fragment is UserHomePageFragment) {
            supportFragmentManager.popBackStackImmediate(NavigationStates.HOME_PAGE.stateText,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
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
        bundle.putInt(Constants.DATE_LAYOUT, R.layout.fragment_date)
        dateFragment.arguments = bundle
        viewModel.setNavigationState(NavigationStates.DATE_PAGE)
    }

}