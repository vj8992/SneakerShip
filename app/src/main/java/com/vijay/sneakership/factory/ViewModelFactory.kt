package com.vijay.sneakership.factory

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.vijay.sneakership.repository.SneakersDatabase
import com.vijay.sneakership.ui.SneakersViewModel

class ViewModelFactory(
  private val sneakersDatabase: SneakersDatabase
): AbstractSavedStateViewModelFactory() {
  override fun <T : ViewModel> create(
    key: String,
    modelClass: Class<T>,
    handle: SavedStateHandle,
  ): T {
    if (modelClass.isAssignableFrom(SneakersViewModel::class.java)) {
      @Suppress("UNCHECKED_CAST")
      return SneakersViewModel(sneakersDatabase) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}