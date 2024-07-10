package com.example.loginsignup

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.UUID

class ProfileActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private var isEditing: Boolean = false
    private lateinit var profilePic: CircleImageView
    private lateinit var mobileTextView: EditText
    private lateinit var dobTextView: EditText
    private lateinit var currentUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        enableEdgeToEdge()

        mAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        currentUser = mAuth.currentUser ?: return

        val uid: String = currentUser.uid
        val Femail: String? = currentUser.email
        val emailTextView: TextView = findViewById(R.id.textView6)
        emailTextView.text = Femail
        mobileTextView = findViewById(R.id.textView10)
        dobTextView = findViewById(R.id.textView12)
        profilePic = findViewById(R.id.imageView7)
        val passChange: TextView = findViewById(R.id.textView8)

        loadProfileData(uid)

        val editButton: TextView = findViewById(R.id.textView16)
        editButton.setOnClickListener {
            if (!isEditing) {
                enableEditing()
            } else {
                disableEditing()
                saveProfileChanges(uid)
            }
        }

        profilePic.setOnClickListener {
            if (isEditing) {
                openImagePicker()
            }
        }

        passChange.setOnClickListener {
            resetPassword(Femail ?: "")
        }
    }

    private fun loadProfileData(uid: String) {
        val mobileNumRef = firestore.collection("Profile").document(uid).get()
        val dobRef = firestore.collection("Profile").document(uid).get()
        val imageRef = firestore.collection("images").document(uid).get()

        mobileNumRef.addOnSuccessListener { document ->
            val mobileNumber = document.getString("mobile")
            mobileNumber?.let { mobileTextView.setText(it) }
        }.addOnFailureListener { exception ->
            Log.d("ProfileActivity", "Error fetching mobile number: $exception")
        }

        dobRef.addOnSuccessListener { document ->
            val dob = document.getString("dob")
            dob?.let { dobTextView.setText(it) }
        }.addOnFailureListener { exception ->
            Log.d("ProfileActivity", "Error fetching date of birth: $exception")
        }

        imageRef.addOnSuccessListener { document ->
            val imageUrl = document.getString("imageUrl")
            if (!imageUrl.isNullOrEmpty()) {
                Picasso.get().load(imageUrl).into(profilePic)
            } else {
                profilePic.setImageResource(R.drawable.file)
            }
        }.addOnFailureListener { exception ->
            Log.d("ProfileActivity", "Error fetching profile image: $exception")
        }
    }

    private fun enableEditing() {
        isEditing = true
        mobileTextView.isEnabled = true
        dobTextView.isEnabled = true
        findViewById<TextView>(R.id.textView16).text = "Save"
    }

    private fun disableEditing() {
        isEditing = false
        mobileTextView.isEnabled = false
        dobTextView.isEnabled = false
        findViewById<TextView>(R.id.textView16).text = "Edit"
    }

    private fun saveProfileChanges(uid: String) {
        val mobile = mobileTextView.text.toString().trim()
        val dob = dobTextView.text.toString().trim()

        val userMap = hashMapOf("mobile" to mobile, "dob" to dob)
        firestore.collection("Profile").document(uid)
            .set(userMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Log.d("ProfileActivity", "Error updating profile: $exception")
                Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
            }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        pickImageLauncher.launch(intent)
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val imageUri = data?.data
            profilePic.setImageURI(imageUri)

            uploadImageToFirestore(imageUri)
        }
    }

    private fun uploadImageToFirestore(imageUri: Uri?) {
        imageUri?.let { uri ->
            val storageRef = FirebaseStorage.getInstance().reference.child("images/${UUID.randomUUID()}")

            storageRef.putFile(uri)
                .addOnSuccessListener { taskSnapshot ->
                    storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        val uid = mAuth.uid ?: return@addOnSuccessListener
                        firestore.collection("images")
                            .document(uid)
                            .set(hashMapOf("imageUrl" to downloadUri.toString()))
                            .addOnSuccessListener {
                                // Image URL successfully updated
                            }
                            .addOnFailureListener { e ->
                                Log.d("ProfileActivity", "Error updating image URL: $e")
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Log.d("ProfileActivity", "Error uploading image: $e")
                }
        }
    }

    private fun resetPassword(email: String) {
        mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Password reset email sent to $email", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "Failed to send password reset email", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
