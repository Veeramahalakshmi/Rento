package com.example.loginsignup

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.loginsignup.R
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

class Book : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book) // Use the XML layout file for BOOKActivity

        // Get references to views
        val ivItemImage: ShapeableImageView = findViewById(R.id.ivItemImage)
        val itemName: TextView = findViewById(R.id.eventNameTextView)
        val ageTextView: TextView = findViewById(R.id.ageEditText)
        val phoneNumberTextView : TextView = findViewById(R.id.phoneNumberEditText)
        val sportsNameTextView : TextView = findViewById(R.id.sportsNameEditText)
        val dateTextView: TextView = findViewById(R.id.dateTextView)
        val sportsCategorySpinner : TextView = findViewById(R.id.sportCategorySpinner)
        val locationTextView: TextView = findViewById(R.id.locationTextView)
        val callButton: Button = findViewById(R.id.callButton)
        val chatButton: Button = findViewById(R.id.chatButton)

        // Get intent extras
        val itemNameExtra = intent.getStringExtra("eventName") // Corrected key
        val ageExtra = intent.getStringExtra("age")
        val phoneNumberExtra = intent.getStringExtra("phoneNumber")
        val sportsNameExtra = intent.getStringExtra("sportsName")
        val dateExtra = intent.getStringExtra("date")
        val sportsCategoryExtra = intent.getStringExtra("sportsCategory")
        val locationExtra = intent.getStringExtra("location")
        val emailExtra = intent.getStringExtra("email")
        val imageUrlExtra = intent.getStringExtra("imageUrl")

        // Set values to views
        itemName.text = itemNameExtra
        ageTextView.text = ageExtra
        phoneNumberTextView.text = phoneNumberExtra
        sportsNameTextView.text = sportsNameExtra
        dateTextView.text = dateExtra
        sportsCategorySpinner.text = sportsCategoryExtra
        locationTextView.text = locationExtra
        Picasso.get().load(imageUrlExtra).into(ivItemImage)

        // Set click listener for call button
        callButton.setOnClickListener {
            val phoneNumber = phoneNumberTextView.text.toString()
            initiateCall(phoneNumber)
        }

        // Set click listener for chat button
        chatButton.setOnClickListener {
            val phoneNumber = phoneNumberTextView.text.toString()
            initiateChat(phoneNumber)
        }
    }

    private fun initiateCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phoneNumber")
        startActivity(intent)
    }

    private fun initiateChat(phoneNumber: String) {
        // Format the phone number for WhatsApp
        val formattedPhoneNumber = phoneNumber.replace("\\s".toRegex(), "")

        // Create the intent
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://wa.me/$formattedPhoneNumber")

        // Check if WhatsApp is installed
        if (isAppInstalled("com.whatsapp")) {
            // Open WhatsApp chat
            intent.`package` = "com.whatsapp"
        } else {
            // Open the default SMS app if WhatsApp is not installed
            intent.data = Uri.parse("sms:$formattedPhoneNumber")
        }

        // Start the activity
        startActivity(intent)
    }

    private fun isAppInstalled(packageName: String): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: Exception) {
            false
        }
    }
}
