package com.example.shop.presentation

import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shop.R
import com.example.shop.domain.ShopItem

class ShopListAdapter: ListAdapter<ShopItem, ShopListAdapter.ShopItemViewHolder>(ShopItemDiffCallback()) {

    var onShopItemOnLongClickListener:((ShopItem)-> Unit)? = null
    var onShopItemOnShortClickListener:((ShopItem)->Unit)?  = null



    class ShopItemViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val tvName = itemView.findViewById<TextView>(R.id.tvName)
        val tvCount = itemView.findViewById<TextView>(R.id.tvCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val id = if (viewType == DISABLED){
            R.layout.item_shop_disabled
        }else{
            R.layout.item_shop_enabled
        }
        val view = LayoutInflater.from(parent.context).inflate(id, parent, false)
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = getItem(position)
        holder.tvName.text = shopItem.name
        holder.tvCount.text = shopItem.count.toString()
        holder.itemView.setOnLongClickListener {
            onShopItemOnLongClickListener?.invoke(shopItem)
            true
        }
        holder.itemView.setOnClickListener {
            onShopItemOnShortClickListener?.invoke(shopItem)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).enable){
            ENABLED
        }else{
            DISABLED
        }
    }
    companion object{
        val DISABLED = 0
        val ENABLED = 1
    }
}