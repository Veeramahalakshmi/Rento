package com.example.loginsignup

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class IntroActivity : AppCompatActivity() {
    private val delayMillis: Long = 1500 // 1.5 seconds delay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        // Delayed redirection using Handler
        Handler().postDelayed({
            startActivity(Intent(this, loginActivity::class.java))
            finish() // Finish current activity so the user cannot go back to it using the back button
        }, delayMillis)

        // Initialize ImageView
        val imageView = findViewById<ImageView>(R.id.imageView)

        // Define the animation
        val animation = ScaleAnimation(
            0f, 1f, // Scale from 0 to 1 on X axis
            0f, 1f, // Scale from 0 to 1 on Y axis
            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point X (center)
            Animation.RELATIVE_TO_SELF, 0.5f // Pivot point Y (center)
        )
        animation.duration = 1000 // Duration in milliseconds
        animation.fillAfter = true // Keeps the final state of the animation

        // Set animation listener if needed
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                // Animation ended, do something if needed
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })

        // Start animation
        imageView.startAnimation(animation)
    }
}
