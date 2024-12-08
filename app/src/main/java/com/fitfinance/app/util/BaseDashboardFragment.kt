package com.fitfinance.app.util

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import com.fitfinance.app.R

abstract class BaseDashboardFragment : Fragment(), SearchView.OnQueryTextListener {
    protected lateinit var menuProvider: MenuProvider
    abstract val filterListFunction: () -> Unit

    protected fun setupMenuItems() {
        menuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.fragment_dashboard_menu, menu)
                if (menu.findItem(R.id.menu_action_search) != null) {
                    val searchItem = menu.findItem(R.id.menu_action_search)
                    val searchView = searchItem.actionView as SearchView
                    searchView.setOnQueryTextListener(this@BaseDashboardFragment)
                }
                if (menu.findItem(R.id.menu_action_filter) != null) {
                    val filterItem = menu.findItem(R.id.menu_action_filter)
                    filterItem.setOnMenuItemClickListener {
                        filterListFunction()
                        true
                    }
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean = true
        }
        requireActivity().addMenuProvider(menuProvider, viewLifecycleOwner)
    }
}