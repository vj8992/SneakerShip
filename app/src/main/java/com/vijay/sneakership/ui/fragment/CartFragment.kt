package com.vijay.sneakership.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vijay.sneakership.Injection
import com.vijay.sneakership.R
import com.vijay.sneakership.R.string
import com.vijay.sneakership.databinding.FragmentCartBinding
import com.vijay.sneakership.model.SneakersModel
import com.vijay.sneakership.ui.SneakersViewModel
import com.vijay.sneakership.ui.activity.HomeActivity
import com.vijay.sneakership.ui.adapter.CartAdapter
import com.vijay.sneakership.ui.adapter.CartAdapter.ItemRemoveListener

class CartFragment: Fragment(), ItemRemoveListener {

  private lateinit var binding: FragmentCartBinding
  private lateinit var viewModel: SneakersViewModel
  private lateinit var adapter: CartAdapter


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProvider(requireActivity(),
      Injection.provideViewModelFactory(requireActivity()))[SneakersViewModel::class.java]
    adapter = CartAdapter(this)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentCartBinding.inflate(layoutInflater, container, false)
    return binding.root
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    (requireActivity() as HomeActivity).apply {
      supportActionBar?.title = getString(string.cart)
      supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    updateViews()

    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
      object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
          Navigation.findNavController(binding.root).navigateUp()
        }
      })
  }

  private fun updateViews(){
    val list = viewModel.getCart()
    if (list.isEmpty()){
      binding.emptyTv.visibility = View.VISIBLE
      binding.mainGroup.visibility = View.GONE
    }else{
      adapter.differ.submitList(list)
      binding.cartRv.apply {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = this@CartFragment.adapter
      }
    }
    binding.subTotalTv.text = "Subtotal : Rs.${viewModel.getSubTotal().toString()}"
    binding.taxTotalTv.text = "Tax and Charges : Rs.${viewModel.getTax().toString()}"
    binding.totalTv.text = "Total : Rs.${viewModel.getTotal()}"

    binding.checkOutBtn.setOnClickListener{
      Toast.makeText(requireContext(), getString(string.check_out_order), Toast.LENGTH_LONG).show()
      viewModel.checkOut()
      findNavController().navigateUp()
    }
  }

  override fun onItemRemoved(sneaker: SneakersModel?) {
    sneaker?.let { viewModel.removeItemFromCart(it) }
    updateViews()
  }
}