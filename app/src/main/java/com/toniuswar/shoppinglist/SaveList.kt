package com.toniuswar.shoppinglist

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText
import android.widget.Toast
import java.io.File


class SaveList (private val context: Context, val productList: MutableList<Product>, var listName: String= ""){
    fun showDialog(){
        val editText = EditText(context)
        editText.setText(listName)

        val alert = AlertDialog.Builder(context)
        alert.setTitle("Назовите список")
        alert.setView(editText)

        alert.setPositiveButton("Сохранить"){x,y ->
            Toast.makeText(context, "Список сохранен", Toast.LENGTH_SHORT).show()
        }
        alert.create().show()
    }

}



