package com.example.shop.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shop.data.ShopListRepositoryImpl
import com.example.shop.domain.DeleteShopItemUseCase
import com.example.shop.domain.EditShopItemUseCase
import com.example.shop.domain.GetShopListUseCase
import com.example.shop.domain.ShopItem

class MainActivityViewModel: ViewModel() {

    private val repository = ShopListRepositoryImpl


    private val getShopListUseCase = GetShopListUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    val shopList = getShopListUseCase.getShopList()

    fun deleteShopItem(shopItem: ShopItem){
        deleteShopItemUseCase.deleteShopItem(shopItem)
    }
    fun changeEnableState(shopItem: ShopItem){
        val newItem = shopItem.copy(enable = !shopItem.enable)
        editShopItemUseCase.editShopItem(newItem)
    }
}