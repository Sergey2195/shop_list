package com.example.shop.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shop.domain.ShopItem
import com.example.shop.domain.ShopListRepository
import kotlin.random.Random

object ShopListRepositoryImpl:ShopListRepository {

    private val shopList = sortedSetOf<ShopItem>({o1, o2-> o1.id.compareTo(o2.id)})
    private val shopLiveData = MutableLiveData<List<ShopItem>>()
    private var currentId = 0
    private val listShops = mutableListOf<ShopItem>()

    init {
        defaultListShops()
        for (item in listShops){
            addShopItem(item)
        }
    }
    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID){
            shopItem.id = currentId++
        }
        shopList.add(shopItem)
        updateLiveData()
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        updateLiveData()
    }

    override fun editShopItem(shopItem: ShopItem) {
        deleteShopItem(getItem(shopItem.id))
        addShopItem(shopItem)
    }

    override fun getItem(shopItemId: Int): ShopItem {
        return  shopList.find { it.id == shopItemId } ?: throw RuntimeException("get item exeption")
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        return shopLiveData
    }

    private fun updateLiveData(){
        shopLiveData.value = shopList.toList()
    }

    private fun defaultListShops(){
        listShops.add(ShopItem("Bread", 2, true))
        listShops.add(ShopItem("Milk", 1, true))
        listShops.add(ShopItem("Egg", 10, false))
        listShops.add(ShopItem("Cheese", 1, true))
        listShops.add(ShopItem("Paper", 1, true))
        listShops.add(ShopItem("Water", 3, true))
        listShops.add(ShopItem("Cucumber", 6, true))
        listShops.add(ShopItem("Butter", 1, true))
        listShops.add(ShopItem("Hum", 2, true))
        listShops.add(ShopItem("Sugar", 4, false))
        listShops.add(ShopItem("Pork", 1, true))
        listShops.add(ShopItem("Beef", 2, true))
    }
}