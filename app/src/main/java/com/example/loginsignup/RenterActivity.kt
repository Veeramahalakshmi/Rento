package com.example.loginsignup

import androidx.appcompat.widget.Toolbar
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class RenterActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_renter)

        // Initialize views
        drawerLayout = findViewById(R.id.drawerlayout)

        // Set up the AppBar with Toolbar
        setSupportActionBar(findViewById(R.id.mytoolbar))

        val toolbar = findViewById<Toolbar>(R.id.mytoolbar)
        toolbar.title = "Rento"
        setSupportActionBar(findViewById(R.id.mytoolbar))
        mAuth = FirebaseAuth.getInstance()

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerlayout)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, findViewById(R.id.mytoolbar), R.string.navigation_drawer_open, R.string.navigation_drawe_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val addItem = findViewById<ImageView>(R.id.additem)
        addItem.setOnClickListener {
            val intent = Intent(this, EventDetailsActivity::class.java)
            startActivity(intent)
        }

        val profileImage = findViewById<ImageView>(R.id.profile)
        profileImage.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        val About = findViewById<ImageView>(R.id.About_Us)
        About.setOnClickListener {
            startActivity(Intent(this, AboutUsActivity::class.java))
        }

        val Signout = findViewById<ImageView>(R.id.Signout)
        Signout.setOnClickListener{
        mAuth.signOut()
        startActivity(Intent(this, loginActivity::class.java))
        finish()}

        val tc =findViewById<ImageView>(R.id.tc)
        tc.setOnClickListener {
            mAuth.signOut()
            startActivity(Intent(this, termsActivity::class.java))
            finish()
        }
        return
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
                startActivity(Intent(this,termsActivity::class.java))
                return true
            }
            R.id.About_Us -> {
                // Start the AboutUsActivity when "About Us" is clicked
                startActivity(Intent(this, AboutUsActivity::class.java))
                return true
            }
            else -> return false
        }
    }
}
