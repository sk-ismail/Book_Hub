package com.example.bookhub.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.bookhub.*
import com.google.android.material.navigation.NavigationView
import com.example.bookhub.fragments.AboutFragment
import com.example.bookhub.fragments.FavouriteFragment
import com.example.bookhub.fragments.FragmentDashboard
import com.example.bookhub.fragments.ProfileFragment

class MainActivity : AppCompatActivity() {

    lateinit var CoordinatorLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView
    lateinit var drawerLayout: DrawerLayout
    var previousmenuitem:MenuItem?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       CoordinatorLayout=findViewById(R.id.coordinatorLayout)
        toolbar=findViewById(R.id.toolbar)
        frameLayout=findViewById(R.id.framelayout)
        navigationView=findViewById(R.id.navigationview)
        drawerLayout=findViewById(R.id.drawerlayout)
        SetUpToolbar()
        val actionBarDrawerToggle= ActionBarDrawerToggle(this@MainActivity,drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.framelayout,
                FragmentDashboard()
            )

            .commit()

        drawerLayout.closeDrawers()
        supportActionBar?.title="Dashboard"

        navigationView.setNavigationItemSelectedListener {

            if (previousmenuitem!=null)
            {
                previousmenuitem?.isChecked=false
            }
            it.isChecked=true
            it.isCheckable=true
            previousmenuitem=it

            when(it.itemId){
                R.id.Dashboard ->{
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.framelayout,
                            FragmentDashboard()
                        )

                        .commit()

                    drawerLayout.closeDrawers()
                    Toast.makeText(this@MainActivity,"Welcome To Dashboard",Toast.LENGTH_SHORT).show()
                     supportActionBar?.title="Dashboard"
                }

                R.id.Favourites ->{

                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.framelayout,
                            FavouriteFragment()
                        )

                        .commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title="Favourites"
                    Toast.makeText(this@MainActivity,"Favourites",Toast.LENGTH_SHORT).show()
                }
                R.id.profile ->{
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.framelayout,
                            ProfileFragment()
                        )

                        .commit()

                    drawerLayout.closeDrawers()
                    supportActionBar?.title="Profile"
                    Toast.makeText(this@MainActivity,"Profile",Toast.LENGTH_SHORT).show()
                }
                R.id.Aboutapp ->
                {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.framelayout,
                        AboutFragment()
                    )
                        .commit()

                    drawerLayout.closeDrawers()
                    supportActionBar?.title="About App"
                    Toast.makeText(this@MainActivity,"AboutApp",Toast.LENGTH_SHORT).show()
                }
            }
            return@setNavigationItemSelectedListener true
        }



    }

    fun SetUpToolbar()
    {
        setSupportActionBar(toolbar)
        supportActionBar?.title="Tool Bar"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

       val id=item.itemId
        if(id==android.R.id.home)
        {
            drawerLayout.openDrawer(GravityCompat.START)
        }


        return super.onOptionsItemSelected(item)

    }

    override fun onBackPressed() {

        val frag=supportFragmentManager.findFragmentById(R.id.framelayout)

        when(frag){
            !is FragmentDashboard -> openDashboard()

        else->super.onBackPressed()
        }
    }

    fun openDashboard(){
            val fragment= FragmentDashboard()
            val transaction=supportFragmentManager.beginTransaction()
        transaction.replace(R.id.framelayout, fragment)
        transaction.commit()
        supportActionBar?.title="Dashboard"
        navigationView.setCheckedItem(R.id.Dashboard)


    }

}

