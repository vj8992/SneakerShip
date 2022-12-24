package com.vijay.sneakership.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.vijay.sneakership.model.SneakersModel
import com.vijay.sneakership.repository.SneakersDatabase
import com.vijay.sneakership.repository.SneakersPagingSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class SneakersViewModel(sneakersDB: SneakersDatabase) : ViewModel() {

  private val cartList = mutableListOf<SneakersModel>()
  private var subTotal: Int = 0
  private var tax: Int = 0
  private var total: Int = 0
  private var cartSize = MutableLiveData<Int>()
  private var selectedSneaker: SneakersModel? = null

  val filterFlow = MutableStateFlow<String>("")
  @OptIn(ExperimentalCoroutinesApi::class)
  val data = filterFlow.flatMapLatest { filter ->
    Pager(
      PagingConfig(
        pageSize = 20,
        enablePlaceholders = false,
        initialLoadSize = 20
      ),
    ) {
      SneakersPagingSource(sneakersDB, filter)
    }.flow.cachedIn(viewModelScope)
  }


  fun addToCart(sneaker: SneakersModel){
    cartList.add(sneaker)
    cartSize.value = cartList.size
      subTotal += sneaker.retailPrice
      tax = subTotal/18
      total = subTotal+tax

  }

  fun checkOut(){
    cartList.clear()
    cartSize.value = 0
  }

  fun getCart(): List<SneakersModel>{
    return cartList
  }

  fun removeItemFromCart(sneaker: SneakersModel){
    cartList.remove(sneaker)
    cartSize.value = cartList.size
    if (subTotal != 0){
      subTotal -= sneaker.retailPrice
      tax = subTotal/18
      total = subTotal+tax
    }
  }

  fun setSelectedItem(sneaker: SneakersModel){
    selectedSneaker = sneaker
  }

  fun getSelectedSneaker(): SneakersModel?{
    return selectedSneaker
  }

  fun getTax(): Int{
    return tax
  }

  fun getSubTotal(): Int{
    return subTotal
  }

  fun getTotal(): Int{
    return total
  }

  fun getCartSize(): MutableLiveData<Int>{
    return cartSize
  }
}