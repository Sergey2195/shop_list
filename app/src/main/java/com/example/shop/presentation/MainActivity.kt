 package com.example.shop.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shop.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

 class MainActivity : AppCompatActivity() {

     private lateinit var viewModel: MainActivityViewModel
     private lateinit var adapter: ShopListAdapter
     private lateinit var newShopItemButton: FloatingActionButton

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_main)
         setup()
         viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
         viewModel.shopList.observe(this){
            adapter.submitList(it)
         }
     }
     private fun setup(){
         val rvShopList = findViewById<RecyclerView>(R.id.rv_shop_list)
         newShopItemButton = findViewById(R.id.button_add_shop_item)
         adapter = ShopListAdapter()
         rvShopList.adapter = adapter
         rvShopList.recycledViewPool.setMaxRecycledViews(ShopListAdapter.DISABLED, 15)
         rvShopList.recycledViewPool.setMaxRecycledViews(ShopListAdapter.ENABLED, 15)
         setupClickListeners()
         setupSwipeListener(rvShopList)
     }

     private fun setupSwipeListener(rvShopList: RecyclerView) {
         val itemTouchHelperCallback =
             object :
                 ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                 override fun onMove(
                     recyclerView: RecyclerView,
                     viewHolder: RecyclerView.ViewHolder,
                     target: RecyclerView.ViewHolder
                 ): Boolean {
                     return false
                 }

                 override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                     val item = adapter.currentList[viewHolder.adapterPosition]
                     viewModel.deleteShopItem(item)
                 }
             }
         val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
         itemTouchHelper.attachToRecyclerView(rvShopList)
     }

     private fun setupClickListeners() {
         adapter.onShopItemOnLongClickListener = {
             viewModel.changeEnableState(it)
         }
         adapter.onShopItemOnShortClickListener = {
             val id = it.id
             val intent = ShopItemActivity.newIntentEditItem(this, id)
             startActivity(intent)
         }
         newShopItemButton.setOnClickListener {
             val intent = ShopItemActivity.newIntentAddItem(this)
             startActivity(intent)
         }
     }
 }