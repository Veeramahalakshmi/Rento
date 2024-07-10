package com.example.loginsignup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
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

class EventDetailsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var eventsRecyclerView: RecyclerView
    private lateinit var eventsAdapter: EventsAdapter
    private lateinit var databaseRef: DatabaseReference


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        // Initialize views
        eventsRecyclerView = findViewById(R.id.eventsRecyclerView)
        eventsRecyclerView.layoutManager = LinearLayoutManager(this)
        eventsAdapter = EventsAdapter()
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
        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, findViewById(R.id.mytoolbar), R.string.navigation_drawer_open, R.string.navigation_drawe_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Fetch and display events from Firebase
        fetchEvents()
    }

    private fun fetchEvents() {
        databaseRef.addValueEventListener(object : ValueEventListener {
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
                Log.e("EventDetailsActivity", "Database error: ${error.message}")
            }
        })
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
        drawerLayout.closeDrawers()
        return true
    }
}
