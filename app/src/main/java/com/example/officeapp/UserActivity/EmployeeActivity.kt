package com.example.officeapp
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.officeapp.*
import com.google.android.material.navigation.NavigationBarView

class EmployeeActivity : AppCompatActivity(), ActivityFragmentConnector
{
    private val homeFragment= UserHomePageFragment.newInstance(this)
    private val profileFragment= UserProfileFragment()
    private val dateFragment= DateFragment(R.layout.fragment_date)

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee)

        val bundle = Bundle()
        bundle.putString(Constants.LOGGEDIN_USERID, intent.getStringExtra(Constants.LOGGEDIN_USERID))
        bundle.putString(Constants.LOGGEDIN_USERNAME, intent.getStringExtra(Constants.LOGGEDIN_USERNAME))
        profileFragment.arguments = bundle

        changeFragment(homeFragment, "Home Page")

        val navigationBarEmployee=findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(
            R.id.navigationBarEmployee)
        navigationBarEmployee.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when(item.itemId)
                {
                    R.id.home ->{
                        val myFragment2: DateFragment? = supportFragmentManager.findFragmentByTag("Date Page") as DateFragment?
                        val myFragment: UserHomePageFragment? = supportFragmentManager.findFragmentByTag("Home Page") as UserHomePageFragment?
                        if(!((myFragment2 != null && myFragment2.isVisible()) || (myFragment != null && myFragment.isVisible()))) {

                            if(!supportFragmentManager.popBackStackImmediate ("Home Page", FragmentManager.POP_BACK_STACK_INCLUSIVE))
                                changeFragment(homeFragment, "Home Page")
                        }
                    }
                    R.id.profile ->{changeFragment(profileFragment, "Profile Page")}
                }
                return true
            }
        })

        supportFragmentManager.addOnBackStackChangedListener {

            val myFragment2: DateFragment? = supportFragmentManager.findFragmentByTag("Date Page") as DateFragment?
            val myFragment: UserHomePageFragment? = supportFragmentManager.findFragmentByTag("Home Page") as UserHomePageFragment?
            if (((myFragment2 != null && myFragment2.isVisible()) || (myFragment != null && myFragment.isVisible())) && navigationBarEmployee.selectedItemId!=R.id.home) {
                navigationBarEmployee.selectedItemId=R.id.home
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