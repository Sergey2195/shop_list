package com.example.shop.domain

class GetItemWithIdUseCase(private val shopListRepository: ShopListRepository) {
    fun getItem(shopItemId: Int): ShopItem{
        return shopListRepository.getItem(shopItemId)
    }
}