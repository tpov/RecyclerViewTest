package com.tpov.testphoto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.tpov.testphoto.first_task.FirstTaskFragment
import com.tpov.testphoto.second_task.SecondTaskFragment

class MainActivity : AppCompatActivity() {

    private lateinit var menu: ImageButton
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var firstTaskFragment: FirstTaskFragment
    private lateinit var secondTaskFragment: SecondTaskFragment
    private lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initTextView()

    }

    private fun initView() {
        menu = findViewById(R.id.btnMenu)
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)

        fragmentManager = FragmentManager(supportFragmentManager, R.id.fragment_container)
        firstTaskFragment = FirstTaskFragment()
        secondTaskFragment = SecondTaskFragment()
        fragmentManager.initFragment(firstTaskFragment)
    }

    private fun initTextView() {

        menu.setOnClickListener { view ->
            if (drawerLayout.isDrawerOpen(GravityCompat.START))
                drawerLayout.closeDrawer(GravityCompat.END)
            else drawerLayout.openDrawer(GravityCompat.START)
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_first_task -> fragmentManager.replaceFragment(firstTaskFragment)
                R.id.action_second_task -> fragmentManager.replaceFragment(secondTaskFragment)
            }
            true
        }
    }
}