package com.vijay.sneakership.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.vijay.sneakership.Injection
import com.vijay.sneakership.R
import com.vijay.sneakership.R.string
import com.vijay.sneakership.databinding.FragmentHomeBinding
import com.vijay.sneakership.model.SneakersModel
import com.vijay.sneakership.ui.SneakersViewModel
import com.vijay.sneakership.ui.activity.HomeActivity
import com.vijay.sneakership.ui.adapter.SneakersAdapter
import com.vijay.sneakership.ui.adapter.SneakersAdapter.ClickListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

class HomeFragment: Fragment(), ClickListener, SearchView.OnQueryTextListener {

  private lateinit var binding: FragmentHomeBinding
  private lateinit var viewModel: SneakersViewModel
  private lateinit var adapter: SneakersAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProvider(requireActivity(),
      Injection.provideViewModelFactory(requireContext()))[SneakersViewModel::class.java]
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
    return binding.root
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    (requireActivity() as HomeActivity).supportActionBar?.title = getString(string.home_title)
    adapter = SneakersAdapter(this)
    binding.sneakersRv.layoutManager = GridLayoutManager(requireContext(), 2)
    binding.sneakersRv.adapter = adapter
    fetchData()
    adapter.addLoadStateListener { loadState->
      if (loadState.source.refresh is LoadState.NotLoading
        || loadState.append.endOfPaginationReached){
        binding.emptyTv.isVisible = adapter.itemCount < 1
      }
      //Hack for first time data load problem
      if (loadState.source.refresh is LoadState.NotLoading && adapter.itemCount < 1
        && viewModel.filterFlow.value.isEmpty()){
        fetchData()
      }
    }
    val menuHost: MenuHost = requireActivity()
    menuHost.addMenuProvider(object : MenuProvider {
      override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        // Add menu items here
        menuInflater.inflate(R.menu.home_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this@HomeFragment)
      }

      override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return true
      }
    }, viewLifecycleOwner, Lifecycle.State.RESUMED)
  }

  private fun fetchData(){
    lifecycleScope.launchWhenCreated {
      viewModel.data.collectLatest {
        adapter.submitData(it)
      }
    }
  }

  override fun onItemAddedToCart(sneaker: SneakersModel?) {
    if (sneaker != null) {
      Toast.makeText(requireContext(), getString(string.added_to_cart), Toast.LENGTH_SHORT).show()
      viewModel.addToCart(sneaker)
    }
  }

  override fun onSneakerClicked(sneaker: SneakersModel?) {
    if (sneaker != null) {
      viewModel.setSelectedItem(sneaker)
      Navigation.findNavController(binding.root).navigate(R.id.action_home_to_details)
    }
  }

  override fun onResume() {
    super.onResume()
    (requireActivity() as HomeActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
  }

  override fun onQueryTextSubmit(query: String): Boolean {
    viewModel.filterFlow.value = query
    return true
  }

  override fun onQueryTextChange(newText: String): Boolean {
    viewModel.filterFlow.value = newText
    return true
  }
}