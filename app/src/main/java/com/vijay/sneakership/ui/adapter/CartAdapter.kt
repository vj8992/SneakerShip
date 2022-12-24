package com.vijay.sneakership.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView.ScaleType.CENTER_INSIDE
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.vijay.sneakership.databinding.ItemCartBinding
import com.vijay.sneakership.model.SneakersModel

class CartAdapter(private val listener: ItemRemoveListener) :
  RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

  private lateinit var binding: ItemCartBinding

  inner class CartViewHolder(private val binding: ItemCartBinding) : RecyclerView.ViewHolder(
    binding.root) {
    init {
      binding.removeFromCartBtn.setOnClickListener {
        listener.onItemRemoved(differ.currentList[bindingAdapterPosition])
      }
    }

    fun bindDataToHolder(sneaker: SneakersModel?) {
      binding.apply {
        sneakerIv.scaleType = CENTER_INSIDE
        sneakerIv.load(sneaker?.imageUrl) {
          crossfade(true)
          transformations(CircleCropTransformation())
        }
        nameTv.text = sneaker?.name
        priceTv.text = "Rs. ${sneaker?.retailPrice.toString()}"
      }
    }
  }

  private val differCallback = object : DiffUtil.ItemCallback<SneakersModel>() {
    override fun areItemsTheSame(
      oldItem: SneakersModel,
      newItem: SneakersModel,
    ): Boolean =
      oldItem.id == newItem.id

    override fun areContentsTheSame(
      oldItem: SneakersModel,
      newItem: SneakersModel,
    ): Boolean =
      oldItem == newItem
  }

  val differ = AsyncListDiffer(this, differCallback)
  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int,
  ): CartViewHolder {
    binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return CartViewHolder(binding)
  }

  override fun onBindViewHolder(
    holder: CartViewHolder,
    position: Int,
  ) {
    holder.bindDataToHolder(differ.currentList[position])
  }

  override fun getItemCount() = differ.currentList.size

  interface ItemRemoveListener{
    fun onItemRemoved(sneaker: SneakersModel?)
  }
}