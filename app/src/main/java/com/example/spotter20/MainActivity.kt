package com.example.spotter20

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var searchButton: Button
    private lateinit var locationSearchBar: EditText
    private lateinit var categoryDropdown: AutoCompleteTextView
    private lateinit var switchToggle: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)  // Loads your XML layout file

        // Initialize location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Initialize views
        locationSearchBar = findViewById(R.id.locationSearchBar)
        categoryDropdown = findViewById(R.id.categoryDropdown)
        searchButton = findViewById(R.id.searchButton)
        switchToggle = findViewById(R.id.switch3)  // Find the Switch by its ID

        // Set up Google Map
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Set up category dropdown
        val categories = arrayOf("Music", "Sports", "Theater", "Comedy", "Festivals")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categories)
        categoryDropdown.setAdapter(adapter)

        // Search button click listener
        searchButton.setOnClickListener {
            fetchEvents()
        }

        // Set listener for the switch to handle toggle events
        switchToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Switch turned ON - Navigate to the New Activity
                val intent = Intent(this, ChartActivity::class.java)
                startActivity(intent)
            } else {
                // Switch turned OFF - Handle the action if needed (in this case, no action is required)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true

        // Request location permission and get user's location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    lastLocation = location
                    val userLatLng = LatLng(location.latitude, location.longitude)
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 12f))
                }
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }

    // Handle permission request result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onMapReady(map) // Reload map if permission granted
        }
    }

    private fun fetchEvents() {
        if (!::lastLocation.isInitialized) {
            Toast.makeText(this, "Location not available yet!", Toast.LENGTH_SHORT).show()
            return
        }

        val latitude = 34.0522
        val longitude = -118.2437

        val selectedCategory = categoryDropdown.text.toString()

        val ticketmasterAPI = "https://app.ticketmaster.com/discovery/v2/events.json"
        val apiKey = "KA3GnExABT04Eh3tt8q2bC6jn4HAT4vr"

        val requestUrl = "$ticketmasterAPI?lat long=$latitude,$longitude&radius=50&classificationName=$selectedCategory&apikey=$apiKey"

        val requestQueue = Volley.newRequestQueue(this)

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, requestUrl, null,
            { response ->
                println("API Response: $response") // Debugging Response

                try {
                    if (response.has("_embedded")) {
                        val events = response.getJSONObject("_embedded").getJSONArray("events")
                        map.clear() // Clear old markers

                        var firstEventLatLng: LatLng? = null

                        for (i in 0 until events.length()) {
                            val event = events.getJSONObject(i)
                            val eventName = event.getString("name")
                            val venue = event.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0)
                            val venueLat = venue.getJSONObject("location").getDouble("latitude")
                            val venueLng = venue.getJSONObject("location").getDouble("longitude")

                            val eventLocation = LatLng(venueLat, venueLng)

                            // Save first event location for zooming
                            if (firstEventLatLng == null) {
                                firstEventLatLng = eventLocation
                            }

                            map.addMarker(MarkerOptions().position(eventLocation).title(eventName))
                        }

                        // Auto-zoom to the first event found
                        if (firstEventLatLng != null) {
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(firstEventLatLng, 10f))
                        }

                        Toast.makeText(this, "Events found!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "No events found near you!", Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Error parsing event data!", Toast.LENGTH_LONG).show()
                }
            },
            { error ->
                Toast.makeText(this, "API Error: ${error.message}", Toast.LENGTH_SHORT).show()
            })

        requestQueue.add(jsonObjectRequest)
    }
}
