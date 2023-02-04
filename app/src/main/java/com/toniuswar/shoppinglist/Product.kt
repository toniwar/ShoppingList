package com.toniuswar.shoppinglist

import android.content.Context
import android.widget.LinearLayout
import android.widget.LinearLayout.*
import android.widget.TextView

class Product(private val context: Context, var name: String, var amount: Int, var weight: Float, var units:String, private val mainLayout: LinearLayout, private val productLayout: LinearLayout = LinearLayout(context)) {
    private val amountTV = TextView(context)
    private val weightTV = TextView(context)
    private val productName = TextView(context)

    fun setProductLayout() {
        productLayout.orientation = HORIZONTAL
        val weightParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        weightParams.weight = 6F
        fillET()
    }
    fun removeFromPL(){
        productLayout.removeAllViews()
    }
    private fun fillET(){
        with(productName){
            text = ""
            textSize = 22F
            text = "  $name"
        }
        amountTV.text = ""
        if(amount > 1){
            with(amountTV){
                textSize = 22F
                text = " $amount шт."
            }
        }
        weightTV.text = ""
        if(weight != 0.0F){
            with(weightTV){
                textSize = 22F
                text = "${if(amount>1)" по" else ""} $weight $units"
            }
        }
        with(productLayout) {
            addView(productName)
            addView(amountTV)
            addView(weightTV)
            mainLayout.addView(this)
        }
    }

}






