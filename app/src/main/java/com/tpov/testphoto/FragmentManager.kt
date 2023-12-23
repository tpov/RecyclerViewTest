package com.tpov.testphoto

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class FragmentManager(private val fragmentManager: FragmentManager, private val containerId: Int) {

    fun initFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.add(containerId, fragment)
        transaction.commit()
    }

    fun replaceFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(containerId, fragment)
        transaction.commit()
    }
}
