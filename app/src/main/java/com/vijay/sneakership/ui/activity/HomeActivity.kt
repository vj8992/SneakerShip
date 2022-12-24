package com.vijay.sneakership.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.view.MenuProvider
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vijay.sneakership.Injection
import com.vijay.sneakership.R
import com.vijay.sneakership.databinding.ActivityHomeBinding
import com.vijay.sneakership.factory.ViewModelFactory
import com.vijay.sneakership.model.SneakersModel
import com.vijay.sneakership.ui.SneakersViewModel

class HomeActivity : AppCompatActivity() {

  private lateinit var binding: ActivityHomeBinding
  private lateinit var viewModel: SneakersViewModel
  private lateinit var navController: NavController

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityHomeBinding.inflate(layoutInflater)
    setContentView(binding.root)
    viewModel =
      ViewModelProvider(this,
        Injection.provideViewModelFactory(this))[SneakersViewModel::class.java]
    val host: NavHostFragment = supportFragmentManager
      .findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return
    navController = host.navController
    val navView  = binding.bottomNavView
    navView.setupWithNavController(navController)
    setupActionBarWithNavController(navController)
    viewModel.getCartSize().observe(this) {
      if (it > 0){
        navView.getOrCreateBadge(R.id.checkOutFragment).number = it
      }else{
        navView.removeBadge(R.id.checkOutFragment)
      }
    }
  }
  override fun onSupportNavigateUp(): Boolean {
    return navController.navigateUp() || super.onSupportNavigateUp()
  }

}