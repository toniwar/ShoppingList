package com.toniuswar.shoppinglist

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.toniuswar.shoppinglist.R.layout

class NewShoppingList() : AppCompatActivity(){
    private var productList:MutableList<Product> = mutableListOf()
    lateinit var mainLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_new_shopping_list)
        mainLayout = findViewById(R.id.mainLayout)
        val addBtn:Button = findViewById(R.id.addProductBtn)
        val saveListBtn: Button = findViewById(R.id.saveListBtn)
        addBtn.setOnClickListener {
          setProduct(this,false)
        }
        saveListBtn.setOnClickListener { SaveList(this, productList).showDialog() }
    }

    private fun setProduct(context: Context, isEdit:Boolean, productName:String = "", amount:Int = 1, weight: Float = 0.0F, weightUnits: String = "кг", strForPositiveBtn:String = "Добавить", ){
        var productIndex = 0
        if(isEdit) {
            productList.forEachIndexed { index, product ->
                if(productName == product.name) productIndex = index
            }
        }
        val paramsForFirstElement = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        val paramsForSecondElement = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        paramsForSecondElement.weight = 2.0F

        val dialogLayout = LinearLayout(context)
        dialogLayout.orientation=LinearLayout.VERTICAL

        val nameLayout = LinearLayout(context)
        nameLayout.orientation = LinearLayout.VERTICAL

        val editsLayout = LinearLayout(context)
        editsLayout.orientation = LinearLayout.HORIZONTAL
        editsLayout.layoutParams = paramsForFirstElement

        val labels = arrayOf("Количество:","Масса:", "Единицы")
        val editFields: MutableList<View> = mutableListOf()

        val labelForNameET = TextView(context)
        labelForNameET.text = "Укажите название:"
        val nameET = EditText(context)
        nameET.inputType = InputType.TYPE_CLASS_TEXT
        nameET.setText(productName)

        nameLayout.addView(labelForNameET)
        nameLayout.addView(nameET)

        val amountET = EditText(context)
        with(amountET) {
            inputType = InputType.TYPE_CLASS_NUMBER
            setText(amount.toString())
            editFields.add(this)
        }

        val weightET = EditText(context)
        with(weightET) {
            inputType = InputType.TYPE_CLASS_NUMBER
            setText(weight.toString())
            editFields.add(this)
        }

        val unitsET = EditText(context)
        with(unitsET) {
            setText(weightUnits)
            editFields.add(this)
        }

        labels.forEachIndexed{index, label -> run{
                val textView = TextView(context)
                val column = LinearLayout(context)
                column.layoutParams = paramsForSecondElement
                column.orientation = LinearLayout.VERTICAL
                with(textView){
                    text  = label
                    textSize = 11F
                    textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                    column.addView(this)
                }
                column.addView(editFields[index])
                editsLayout.addView(column)
            }
        }
        dialogLayout.addView(nameLayout)
        dialogLayout.addView(editsLayout)

        val alert:AlertDialog.Builder = AlertDialog.Builder(context)
        alert.setView(dialogLayout)
        alert.setPositiveButton(strForPositiveBtn){ x, y ->
            if(nameET.text.toString() == "") return@setPositiveButton
            val amount = try {
               amountET.text.toString().toInt()
            }
            catch (e:NumberFormatException){
                1
            }
            val weight = try {
                weightET.text.toString().toFloat()
            }
            catch (e:NumberFormatException){
                0.0F
            }
            addProductToList(nameET.text.toString(), amount, weight, unitsET.text.toString(), isEdit, productIndex,)

        }
        alert.create().show()
    }

    private fun addProductToList(name:String, amount:Int = 1, weight:Float = 0.0F, units:String = "кг", isEdit: Boolean, productIndex:Int, ) {
        val productLayout = LinearLayout(this)
        val newProduct = Product(this, name.replaceFirstChar { it.uppercase() }, amount, weight, units, mainLayout,productLayout)
        productLayout.setOnClickListener { showProductTools(newProduct) }
        if(!isEdit) {
            if (this.productList.isEmpty()) {
                this.productList.add(newProduct)
                productList[0].setProductLayout()
                return
            }
            productList.forEach {
                if (it.name.replace("ё", "е", true)
                        .equals(newProduct.name.replace("ё", "е", true), true)
                ) {
                    Toast.makeText(this, "Данный товар уже есть в списке", Toast.LENGTH_SHORT)
                        .show()
                    return
                }
            }
            this.productList.add(newProduct)
            productList.forEach { it.removeFromPL() }
            mainLayout.removeAllViews()
            productList.forEach { it.setProductLayout() }
        }
        else{
            productList.forEachIndexed { index, product ->
                if(product.name == newProduct.name && index != productIndex){
                    Toast.makeText(this, "Данный товар уже есть в списке", Toast.LENGTH_SHORT)
                        .show()
                    return
                }
            }
            productList[productIndex] = newProduct
            productList.forEach { it.removeFromPL() }
            mainLayout.removeAllViews()
            productList.forEach { it.setProductLayout() }
        }
    }

    private fun showProductTools(product: Product){

        val alert = AlertDialog.Builder(this)
        alert.setNegativeButton("Удалить") { x, y ->
                productList.forEach {
                    it.removeFromPL()
                }
                productList.remove(product)
                mainLayout.removeAllViews()
                productList.forEach { it.setProductLayout() }

            }

        alert.setPositiveButton("Редактировать") { x, y ->

            setProduct(this,true, product.name, product.amount, product.weight, product.units, "Редактировать")
        }
        alert.create().show()
    }
}
