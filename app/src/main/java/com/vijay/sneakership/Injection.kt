package com.vijay.sneakership

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.vijay.sneakership.factory.ViewModelFactory
import com.vijay.sneakership.repository.SneakersDatabase

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */

object Injection {

  private fun provideSneakersDatabase(context: Context): SneakersDatabase{
    return SneakersDatabase.getInstance(context)
  }

  fun provideViewModelFactory(context: Context): ViewModelProvider.Factory{
    return ViewModelFactory(provideSneakersDatabase(context))
  }
}