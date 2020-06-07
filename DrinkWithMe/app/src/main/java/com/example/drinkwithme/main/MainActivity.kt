package com.example.drinkwithme.main

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.drinkwithme.R
import com.example.drinkwithme.ui.fragments.PersonalDataFragment

class MainActivity : AppCompatActivity() {

    private lateinit var mPreferences: SharedPreferences
    private val sharedPreferencesFile = "com.example.drinkwithme"
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        mPreferences = getSharedPreferences(sharedPreferencesFile, MODE_PRIVATE)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PersonalDataFragment.newInstance())
                .commit()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return false
    }

}
