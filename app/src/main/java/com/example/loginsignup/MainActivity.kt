package com.example.loginsignup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun torenter(view: View)
    {
        val intent = Intent(this, RenterActivity::class.java)
        startActivity(intent)
    }
    fun torentee(view:View)
    {
        val intent=Intent(this,Rentee::class.java)
        startActivity(intent)
    }

}