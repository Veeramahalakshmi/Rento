package com.example.loginsignup

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.util.*
import android.text.Editable


class RentAdd : AppCompatActivity() {

    private lateinit var eventNameEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var sportsNameEditText: EditText
    private lateinit var sportCategorySpinner: Spinner
    private lateinit var ivItemImage: ImageView
    private lateinit var locationEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var calendar: Calendar
    private lateinit var btnAction: Button
    private var userID: String = ""
    private var imageUri: Uri? = null

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
        private lateinit var mAuth: FirebaseAuth

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rentadd)

        // Initialize views
        emailEditText = findViewById(R.id.emailEditText)
        eventNameEditText = findViewById(R.id.eventNameEditText)
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText)
        sportsNameEditText = findViewById(R.id.sportsNameEditText)
        sportCategorySpinner = findViewById(R.id.sportCategorySpinner)
        locationEditText = findViewById(R.id.locationEditText)
        ageEditText = findViewById(R.id.ageEditText)
        ivItemImage = findViewById(R.id.ivItemImage)
        btnAction = findViewById(R.id.submitButton)
        calendar = Calendar.getInstance()
        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser

// Check if the user is signed in
        if (currentUser != null) {
            // Get user's email
            val email = currentUser.email
            // Use the email as needed
            emailEditText.text = Editable.Factory.getInstance().newEditable(email)
        } else {
            // No user is signed in
            emailEditText.setEnabled(true);
            emailEditText.setFocusableInTouchMode(true);
        }


        // Set input type for numeric fields to enable suggestions
        phoneNumberEditText.inputType = EditorInfo.TYPE_CLASS_NUMBER
        ageEditText.inputType = EditorInfo.TYPE_CLASS_NUMBER

        ivItemImage.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE_REQUEST
            )
        }

        btnAction.setOnClickListener {
            saveEventToFirebase()
        }

        calendar = Calendar.getInstance()

        // Set up spinner
        val sportsCategories = arrayOf("bikes", "cars", "gadgets", "sports kit", "music kits", "Others")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sportsCategories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sportCategorySpinner.adapter = adapter

        findViewById<Button>(R.id.datePickerButton).setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val date = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                findViewById<Button>(R.id.datePickerButton).text = formatDate(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        date.datePicker.minDate = System.currentTimeMillis() - 1000
        date.show()
    }

    private fun formatDate(date: Date): String {
        val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(date)
    }

    private fun saveEventToFirebase() {
        val eventName = eventNameEditText.text.toString()
        val phoneNumber = phoneNumberEditText.text.toString()
        val sportsName = sportsNameEditText.text.toString()
        val sportCategory = sportCategorySpinner.selectedItem.toString()
        val date = formatDate(calendar.time)
        val location = locationEditText.text.toString()
        val email = emailEditText.text.toString()
        val age = ageEditText.text.toString().toIntOrNull() ?: 0

        userID = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val database = FirebaseDatabase.getInstance()
        val eventsRef = database.getReference("events")

        if (TextUtils.isEmpty(eventName) || TextUtils.isEmpty(sportsName) || TextUtils.isEmpty(sportCategory) || TextUtils.isEmpty(location) || imageUri == null) {
            Toast.makeText(this, "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show()
            return
        }

        val storageRef: StorageReference = FirebaseStorage.getInstance().reference.child("images").child(userID).child(imageUri?.lastPathSegment!!)

        storageRef.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot ->
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    val event = Event(eventName, phoneNumber, sportsName, sportCategory, date, location, email, age, imageUrl)
                    saveEventToDatabase(event)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@RentAdd, "Failed to upload image: " + e.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveEventToDatabase(event: Event) {
        val database = FirebaseDatabase.getInstance()
        val eventsRef = database.getReference("events")
        val eventRef = eventsRef.push()
        eventRef.setValue(event)
            .addOnSuccessListener {
                // Show a dialog with the event details
                val eventDetails = "Name: ${event.eventName}\n" +
                        "Phone Number: ${event.phoneNumber}\n" +
                        "Email: ${event.email}\n" +
                        "Event Name: ${event.sportsName}\n" +
                        "Sport Category: ${event.sportCategory}\n" +
                        "Date: ${event.date}\n" +
                        "Location: ${event.location}\n" +
                        "Age: ${event.age}\n"

                AlertDialog.Builder(this)
                    .setTitle("Event Details")
                    .setMessage(eventDetails)
                    .setPositiveButton("OK") { _, _ ->
                        // Clear EditText fields after saving
                        eventNameEditText.text.clear()
                        phoneNumberEditText.text.clear()
                        sportsNameEditText.text.clear()
                        locationEditText.text.clear()
                        emailEditText.text.clear()
                        ageEditText.text.clear()

                        // Show success message
                        Toast.makeText(this, "Event saved successfully", Toast.LENGTH_SHORT).show()
                    }
                    .show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@RentAdd, "Failed to add event: " + e.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            Picasso.get().load(imageUri).into(ivItemImage)
        }
    }

    data class Event(
        val eventName: String,
        val phoneNumber: String,
        val sportsName: String,
        val sportCategory: String,
        val date: String,
        val location: String,
        val email: String,
        val age: Int,
        val imageUrl: String
    )
}
