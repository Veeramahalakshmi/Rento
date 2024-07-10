package com.example.loginsignup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginsignup.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.signupButton.setOnClickListener {
            val signupUsername = binding.signupUsername.text.toString()
            val signupPassword = binding.signupPassword.text.toString()

            val usernameLengthValid = signupUsername.length >= 8
            val passwordLengthValid = signupPassword.length >= 6

            if (usernameLengthValid && passwordLengthValid) {
                if (isValidEmail(signupUsername)) {
                    signupUser(signupUsername, signupPassword)
                } else {
                    Toast.makeText(
                        this@SignupActivity,
                        "Please enter a valid Gmail address",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                val errorMessage = when {
                    !usernameLengthValid -> "Username should be at least 8 characters long"
                    else -> "Password should be at least 6 characters long"
                }
                Toast.makeText(
                    this@SignupActivity,
                    errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.loginRedirect.setOnClickListener {
            startActivity(Intent(this@SignupActivity, loginActivity::class.java))
            finish()
        }
    }

    private fun signupUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign up success, update UI with the signed-up user's information
                    Log.d("SignupActivity", "createUserWithEmail:success")
                    Toast.makeText(
                        baseContext, "Signup Successful",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@SignupActivity, loginActivity::class.java))
                    finish()
                } else {
                    // If sign up fails, display a message to the user.
                    Log.w("SignupActivity", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Signup failed. ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@gmail\\.com".toRegex()
        return emailPattern.matches(email)
    }
}
