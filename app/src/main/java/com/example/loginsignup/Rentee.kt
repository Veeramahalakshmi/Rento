package com.example.loginsignup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth

class Rentee : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rentee)

        // Initialize FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance()

        // Find ImageViews by their IDs
        val imageView1 = findViewById<ImageView>(R.id.additem)
        val imageView2 = findViewById<ImageView>(R.id.profile)
        val imageView3 = findViewById<ImageView>(R.id.youritems)
        val imageView4 = findViewById<ImageView>(R.id.Signout)

        // Set OnClickListener for each ImageView
        imageView1.setOnClickListener {
            // Navigate to a new page (Replace AddActivity with your desired activity)
            val intent = Intent(this@Rentee, RentAdd::class.java)
            startActivity(intent)
        }
        imageView2.setOnClickListener {
            // Navigate to another new page
            val intent = Intent(this@Rentee, ProfileActivity::class.java)
            startActivity(intent)
        }
        imageView3.setOnClickListener {
            // Navigate to yet another new page
            val intent = Intent(this@Rentee, RenteeUpdateDelete::class.java)
            startActivity(intent)
        }
        imageView4.setOnClickListener {
            // Sign out and navigate to loginActivity
            mAuth.signOut()
            startActivity(Intent(this@Rentee, loginActivity::class.java))
            finish()
        }
    }
}
