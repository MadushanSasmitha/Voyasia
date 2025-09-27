// Create a new Kotlin file, e.g., BaseActivity.kt
package com.example.voyasia

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.ads.mediationtestsuite.activities.HomeActivity
import com.google.android.material.bottomnavigation.BottomNavigationView // If using BottomNavigationView

abstract class BaseActivity : AppCompatActivity() {

    // You might not need to define these here if you only access them within setupBottomNavigationBar
    // but useful if you need to interact with them from subclasses.
    protected var navHomeButton: ImageButton? = null
    protected var navHotelsButton: ImageButton? = null
    protected var navRideButton: ImageButton? = null
    protected var navPlannerButton: ImageButton? = null
    protected var navProfileButton: ImageButton? = null

    // If using BottomNavigationView
    protected var bottomNavigationView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Note: setContentView will be called in the subclasses
    }

    /**
     * Call this method in the onCreate of your subclasses AFTER setContentView.
     * It assumes your navigation_bar.xml is included in the activity's layout
     * and has the specified IDs.
     */
    protected fun setupNavigationBar() {
        // --- Example for ImageButtons ---
        navHomeButton = findViewById(R.id.nav_home) // Make sure this ID exists in navigation_bar.xml
        navHotelsButton = findViewById(R.id.nav_hotels)
        navRideButton = findViewById(R.id.nav_ride)
        navPlannerButton = findViewById(R.id.nav_planner)
        navProfileButton = findViewById(R.id.nav_profile)

        navHomeButton?.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            })
        }

        navHotelsButton?.setOnClickListener {
            if (this !is HotelsActivity) { // Assuming HotelsActivity.kt
                startActivity(Intent(this, HotelsActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                })
            }
        }

        navRideButton?.setOnClickListener {
            if (this !is RequestRideActivity) { // Assuming RequestRideActivity.kt
                startActivity(Intent(this, RequestRideActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                })
            }
        }

        navPlannerButton?.setOnClickListener {
            if (this !is ItineraryPlannerActivity) { // Assuming ItineraryPlannerActivity.kt
                startActivity(Intent(this, ItineraryPlannerActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                })
            }
        }

        navProfileButton?.setOnClickListener {
            if (this !is ProfileActivity) {
                startActivity(Intent(this, ProfileActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                })
            }
        }

        // Highlight active item (simple example, more robust solution might be needed)
        updateNavigationBarState()


        // --- Example for BottomNavigationView ---
        // bottomNavigationView = findViewById(R.id.bottom_navigation) // ID from your navigation_bar.xml
        // bottomNavigationView?.setOnItemSelectedListener { item ->
        //     when (item.itemId) {
        //         R.id.menu_item_home -> { // ID from your menu resource for BottomNavigationView
        //             if (this !is HomeActivity) {
        //                 startActivity(Intent(this, HomeActivity::class.java).apply {
        //                     flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        //                 })
        //             }
        //             true
        //         }
        //         R.id.menu_item_hotels -> {
        //             if (this !is HotelsActivity) {
        //                 startActivity(Intent(this, HotelsActivity::class.java).apply {
        //                     flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        //                 })
        //             }
        //             true
        //         }
        //         // Add other cases for ride, planner, profile
        //         else -> false
        //     }
        // }
        // updateBottomNavigationState() // Call a similar method for BottomNavigationView
    }

    /**
     * Simple method to highlight the current activity's button.
     * You might need a more sophisticated way to manage this, especially with themes.
     */
    private fun updateNavigationBarState() {
        // Reset all buttons to a default state (e.g., alpha, background)
        val buttons = listOfNotNull(navHomeButton, navHotelsButton, navRideButton, navPlannerButton, navProfileButton)
        buttons.forEach { button ->
            button.alpha = 0.7f // Example: dim inactive buttons
            // Or change background: button.setBackgroundResource(R.drawable.nav_button_default_bg)
        }

        // Highlight the current one
        when (this) {
            is HomeActivity -> navHomeButton?.alpha = 1.0f
            is HotelsActivity -> navHotelsButton?.alpha = 1.0f
            is RequestRideActivity -> navRideButton?.alpha = 1.0f
            is ItineraryPlannerActivity -> navPlannerButton?.alpha = 1.0f
            is ProfileActivity -> navProfileButton?.alpha = 1.0f
        }
    }

}

