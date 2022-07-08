package com.example.shop.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.shop.R
import com.example.shop.domain.ShopItem
import com.google.android.material.textfield.TextInputLayout

class ShopItemActivity : AppCompatActivity() {
    private lateinit var tlName: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var tlCount: TextInputLayout
    private lateinit var etCount: EditText
    private lateinit var saveBtn: Button
    private lateinit var viewModel: ShopItemActivityViewModel
    var screenMode = UNKNOWN_MODE
    var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        viewModel = ViewModelProvider(this).get(ShopItemActivityViewModel::class.java)
        init_views()
        parseIntent()
        when (screenMode){
            ADD_NEW->{
                launchAddMode()
            }
            EDIT_ITEM->{
                launchEditMode()
            }
        }
        clickListenersCorrectly()
        viewModel.errorInputName.observe(this){
            if (it){
                tlName.error = "error name"
            }else {
                tlName.error = null
            }
        }
        viewModel.errorInputCount.observe(this){
            if (it){
                tlCount.error = "error count"
            } else {
                tlCount.error = null
            }
        }
        viewModel.canClose.observe(this){
            finish()
        }
    }

    private fun launchEditMode(){
        viewModel.getShopItem(shopItemId)
        viewModel.shopItem.observe(this){ item->
            etName.setText(item.name)
            etCount.setText(item.count.toString())
        }
        saveBtn.setOnClickListener {
            viewModel.editShopItem(etName.text.toString(), etCount.text.toString())
        }
    }

    private fun launchAddMode(){
        saveBtn.setOnClickListener {
            viewModel.addShopItem(etName.text.toString(), etCount.text.toString())
        }
    }

    private fun clickListenersCorrectly(){
        etName.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        etCount.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    private fun parseIntent(){
        screenMode = intent.getStringExtra(MODE) ?: UNKNOWN_MODE
        if (!intent.hasExtra(MODE)){
            throw RuntimeException("No mode")
        }
        val mode = intent.getStringExtra(MODE)
        if (mode != ADD_NEW && mode != EDIT_ITEM){
            throw RuntimeException("Unknown mode $mode")
        }
        screenMode = mode
        if (screenMode == EDIT_ITEM){
            if (!intent.hasExtra(ITEM_ID)){
                throw RuntimeException("ID is absent")
            } else {
               shopItemId = intent.getIntExtra(ITEM_ID, ShopItem.UNDEFINED_ID)
            }
        }
    }
    fun init_views(){
        tlName = findViewById(R.id.til_name)
        etName = findViewById(R.id.et_name)
        tlCount = findViewById(R.id.til_count)
        etCount = findViewById(R.id.et_count)
        saveBtn = findViewById(R.id.save_btn)
    }

    companion object{
        private val MODE = "mode"
        private val ADD_NEW = "new item"
        private val EDIT_ITEM = "edit item"
        private val ITEM_ID = "item id"
        private val UNKNOWN_MODE = ""

        fun newIntentAddItem(context: Context):Intent{
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(MODE, ADD_NEW)
            return intent
        }

        fun newIntentEditItem(context: Context, id: Int): Intent{
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(MODE, EDIT_ITEM)
            intent.putExtra(ITEM_ID, id)
            return intent
        }
    }
}