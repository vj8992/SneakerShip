package com.vijay.sneakership.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView.ScaleType.CENTER_INSIDE
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.vijay.sneakership.databinding.ItemSneakerBinding
import com.vijay.sneakership.model.SneakersModel

class SneakersAdapter(private val clickListener: ClickListener):
  PagingDataAdapter<SneakersModel, SneakersAdapter.SneakerViewHolder>(DIFF_CALLBACK) {

  companion object {
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SneakersModel>() {
      override fun areItemsTheSame(oldItem: SneakersModel, newItem: SneakersModel): Boolean =
        oldItem.id == newItem.id

      override fun areContentsTheSame(oldItem: SneakersModel, newItem: SneakersModel): Boolean =
        oldItem == newItem
    }
  }

  inner class SneakerViewHolder(private val binding: ItemSneakerBinding) : RecyclerView.ViewHolder(binding.root){
    init {
      binding.addToCartBtn.setOnClickListener{
        clickListener.onItemAddedToCart(getItem(bindingAdapterPosition))
      }

      binding.container.setOnClickListener{
        clickListener.onSneakerClicked(getItem(bindingAdapterPosition))
      }
    }

    fun bindDataToHolder(sneaker: SneakersModel?){
      binding.apply {
        sneakerIv.scaleType = CENTER_INSIDE
        sneakerIv.load(sneaker?.imageUrl){
          crossfade(true)
          transformations(CircleCropTransformation())
        }
        nameTv.text = sneaker?.name
        priceTv.text = "Rs. ${sneaker?.retailPrice.toString()}"

      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SneakerViewHolder {
    return SneakerViewHolder(
      ItemSneakerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
  }

  override fun onBindViewHolder(holder: SneakerViewHolder, position: Int) {
    val sneaker = getItem(position)

    holder.bindDataToHolder(sneaker)
  }
  interface ClickListener {
    fun onItemAddedToCart(sneaker: SneakersModel?)
    fun onSneakerClicked(sneaker: SneakersModel?)
  }
}