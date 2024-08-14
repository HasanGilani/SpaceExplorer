package com.example.spaceexplorer

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    private val planets = arrayOf("Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ListView to display planets
        val listView: ListView = findViewById(R.id.planet_list_view)
        val adapter = ArrayAdapter(this, R.layout.list_item_planet, R.id.planet_name, planets)
        listView.adapter = adapter

        // Handle item clicks
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, PlanetDetailActivity::class.java)
            intent.putExtra("PLANET_NAME", planets[position])
            startActivity(intent)
        }

        // Load the fragment
        loadFragment(SpaceFactFragment())
    }

    // Method to load a fragment
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    // Inflate the menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // Handle menu item clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_home -> {
                // Handle Home action
                true
            }
            R.id.menu_settings -> {
                // Handle Settings action
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}