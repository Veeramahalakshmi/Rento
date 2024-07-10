package com.example.loginsignup

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RenteeUpdateDelete : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var eventsRecyclerView: RecyclerView
    private lateinit var eventsAdapter: DeleteEventAdapter
    private lateinit var databaseRef: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rentee_update_delete)

        // Initialize views
        eventsRecyclerView = findViewById(R.id.RecyclerView) // This is line 53
        eventsRecyclerView.layoutManager = LinearLayoutManager(this)
        eventsAdapter = DeleteEventAdapter()
        eventsRecyclerView.adapter = eventsAdapter

        setSupportActionBar(findViewById(R.id.mytoolbar))

        val toolbar: Toolbar? = findViewById(R.id.toolbar)
        if (toolbar != null) {
            setSupportActionBar(toolbar)
            supportActionBar?.title = "Rento"
        } else {
            // Handle the case where the toolbar is null
        }

        // Initialize Firebase reference
        databaseRef = FirebaseDatabase.getInstance().getReference("events")
        // Setup navigation drawer



        // Fetch and display events from Firebase
        fetchEvents()
    }

    private fun fetchEvents() {
        mAuth=FirebaseAuth.getInstance()
        val userid = mAuth.currentUser
        val email = userid?.email

        if (email != null) {
            databaseRef.orderByChild("email").equalTo(email)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val eventsList = mutableListOf<Event>()
                        for (eventSnapshot in snapshot.children) {
                            val event = eventSnapshot.getValue(Event::class.java)
                            event?.let {
                                eventsList.add(it)
                            }
                        }
                        eventsAdapter.submitList(eventsList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("RenteeUpdateDelete", "Database error: ${error.message}")
                    }
                })
        } else {
            Log.e("RenteeUpdateDelete", "User email is null.")
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation item clicks here
        when (item.itemId) {
            R.id.Profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                return true
            }
            R.id.Signout -> {
                mAuth.signOut()
                startActivity(Intent(this, loginActivity::class.java))
                finish()
                return true
            }
            R.id.Settings -> {
                startActivity(Intent(this, termsActivity::class.java))
                return true
            }
            R.id.About_Us -> {
                // Start the AboutUsActivity when "About Us" is clicked
                startActivity(Intent(this, AboutUsActivity::class.java))
                return true
            }
            else -> return false
        }

        return true
    }
}
