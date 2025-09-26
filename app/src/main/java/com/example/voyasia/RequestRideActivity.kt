package com.example.voyasia

import android.os.Bundle
// Import other necessary classes

class RequestRideActivity : BaseActivity() { // Inherit from BaseActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.request_ride) // Your layout for the hotels screen

        // Other setup for HotelsActivity...

        setupNavigationBar() // Call the method from BaseActivity
    }

    // Optional: for BottomNavigationView highlighting
    // override fun getNavigationMenuItemId(): Int {
    //     return R.id.menu_item_hotels // ID of hotels item in your BottomNav menu
    // }
}

