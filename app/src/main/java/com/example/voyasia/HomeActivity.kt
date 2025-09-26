package com.example.voyasia

import android.os.Bundle
// Import other necessary classes

class HomeActivity : BaseActivity() { // Inherit from BaseActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home) // Your layout for the home screen

        // Other setup for HomeActivity...

        setupNavigationBar() // Call the method from BaseActivity
    }

    // Optional: for BottomNavigationView highlighting
    // override fun getNavigationMenuItemId(): Int {
    //     return R.id.menu_item_home // ID of home item in your BottomNav menu
    // }
}