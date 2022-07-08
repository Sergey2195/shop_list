package com.example.shop.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shop.data.ShopListRepositoryImpl
import com.example.shop.domain.AddShopItemUseCase
import com.example.shop.domain.EditShopItemUseCase
import com.example.shop.domain.GetItemWithIdUseCase
import com.example.shop.domain.ShopItem
import java.lang.NullPointerException

class ShopItemActivityViewModel(): ViewModel() {

    private val _errorInputName = MutableLiveData<Boolean>()
    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName
    private val _canClose = MutableLiveData<Unit>()
    val canClose : LiveData<Unit>
        get() = _canClose


    private val repository = ShopListRepositoryImpl
    val addShopItemUseCase = AddShopItemUseCase(repository)
    val getShopItemUseCase = GetItemWithIdUseCase(repository)
    val editShopItem = EditShopItemUseCase(repository)

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    fun getShopItem(shopItemId: Int){
        val item = getShopItemUseCase.getItem(shopItemId)
        _shopItem.value = item
    }
    fun addShopItem(inputName: String?, inputCount: String?){
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name,count)
        if (fieldsValid){
            val shopItem = ShopItem(name, count, true)
            addShopItemUseCase.addShopItem(shopItem)
            chCanClose()
        }
    }

    fun editShopItem(inputName: String?, inputCount: String?){
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name,count)
        if (fieldsValid){
            _shopItem.value?.let {
                val item = it.copy(name = name, count = count)
                editShopItem.editShopItem(item)
                chCanClose()
            }
        }
    }

    private fun parseName(inputName: String?): String{
        return inputName?.trim() ?: ""
    }

    private fun parseCount(inputCount: String?): Int {
        return try {
            inputCount?.trim()?.toInt() ?: 0
        } catch (e: Exception){
            0
        }
    }

    private fun validateInput(name: String, count: Int): Boolean{
        var result = true
        if (name.isBlank()){
            _errorInputName.value = true
            result = false
        }
        if (count <= 0){
            _errorInputCount.value = true
            result = false
        }
        return result
    }
    fun resetErrorInputName(){
        _errorInputName.value = false
    }
    fun resetErrorInputCount(){
        _errorInputCount.value = false
    }

    fun chCanClose(){
        _canClose.value = Unit
    }
}