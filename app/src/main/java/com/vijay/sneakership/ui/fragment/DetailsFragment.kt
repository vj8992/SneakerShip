package com.vijay.sneakership.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView.ScaleType.CENTER
import android.widget.ImageView.ScaleType.FIT_CENTER
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import coil.load
import coil.transform.CircleCropTransformation
import com.vijay.sneakership.Injection
import com.vijay.sneakership.R.string
import com.vijay.sneakership.databinding.FragmentDetailsBinding
import com.vijay.sneakership.ui.SneakersViewModel
import com.vijay.sneakership.ui.activity.HomeActivity

class DetailsFragment: Fragment() {

  private lateinit var binding: FragmentDetailsBinding
  private lateinit var viewModel: SneakersViewModel

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
    binding = FragmentDetailsBinding.inflate(layoutInflater, container, false)
    return binding.root
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    (requireActivity() as HomeActivity).apply {
      supportActionBar?.title = getString(string.details)
      supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    super.onViewCreated(view, savedInstanceState)
    val sneaker = viewModel.getSelectedSneaker()
    binding.sneakerIv.scaleType = FIT_CENTER
    binding.sneakerIv.load(sneaker?.imageUrl){
      crossfade(true)
      transformations(CircleCropTransformation())
    }
    binding.nameTv.text = sneaker?.name
    binding.detailsTv.text = sneaker?.title
    binding.colorTv.text = "Color : ${sneaker?.colorway}"
    binding.priceTv.text = "Price : Rs.${sneaker?.retailPrice.toString()}"
    binding.releaseDateTv.text = "Release Date : ${sneaker?.releaseDate}"

    binding.addToCartBtn.setOnClickListener{
      Toast.makeText(requireContext(), getString(string.added_to_cart), Toast.LENGTH_SHORT).show()
      if (sneaker != null) {
        viewModel.addToCart(sneaker)
      }
      Navigation.findNavController(binding.root).navigateUp()
    }

    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
    object :OnBackPressedCallback(true){
      override fun handleOnBackPressed() {
        Navigation.findNavController(binding.root).navigateUp()
      }
    })
  }
}