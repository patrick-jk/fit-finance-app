package com.fitfinance.app.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.view.MenuProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.fitfinance.app.R
import com.fitfinance.app.databinding.ActivityMainBinding
import com.fitfinance.app.presentation.ui.aboutus.AboutUsActivity
import com.fitfinance.app.presentation.ui.login.LoginActivity
import com.fitfinance.app.presentation.ui.profile.ProfileFragment
import com.fitfinance.app.presentation.ui.profile.changepassword.ChangePasswordFragment
import com.fitfinance.app.util.SHARED_PREF_NAME
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_finances, R.id.navigation_investments
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        setupActionBar()

        supportFragmentManager.setFragmentResultListener(ChangePasswordFragment.REQUEST_FINISH_SESSION, this) { _, _ ->
            finishSession()
        }
    }

    private fun setupActionBar() {
        addMenuProvider(object : MenuProvider {
            @SuppressLint("RestrictedApi")
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                if (menu is MenuBuilder) menu.setOptionalIconsVisible(true)
                menuInflater.inflate(R.menu.activity_main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menu_logout_account -> {
                        MaterialAlertDialogBuilder(this@MainActivity)
                            .setTitle(getString(R.string.menu_logout_app))
                            .setMessage(getString(R.string.txt_action_logout))
                            .setPositiveButton(android.R.string.ok) { _, _ ->
                                finishSession()
                                finish()
                            }
                            .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                                dialog.dismiss()
                            }.show()
                    }

                    R.id.menu_user_profile -> {
                        ProfileFragment.newInstance().show(supportFragmentManager, ProfileFragment::class.java.simpleName)
                    }

                    R.id.menu_about_us -> {
                        startActivity(Intent(this@MainActivity, AboutUsActivity::class.java))
                    }
                }
                return true
            }
        })
    }

    private fun finishSession() {
        getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE).edit().clear().apply()
        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
    }
}