package com.toniuswar.shoppinglist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    var isUnsavedList:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val newListBtn:Button = findViewById(R.id.newList)
        newListBtn.setOnClickListener { checkUnsavedList() }
    }

    private fun checkUnsavedList(){
        if(!isUnsavedList) createNewList()
    }

    private fun createNewList(){
        val newList: Intent = Intent(this, NewShoppingList::class.java)
        startActivity(newList)
    }




}