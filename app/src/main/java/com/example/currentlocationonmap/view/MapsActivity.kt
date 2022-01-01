package com.example.currentlocationonmap.view

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.currentlocationonmap.R
import com.example.currentlocationonmap.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

//    var address: String? = null
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    var currentLocation: Location? = null
    var currentMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()

    }

    private fun fetchLocation() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (ActivityCompat.checkSelfPermission(
                    this, android.Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this, android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1000
                )

                return
            }
        }
        val task = fusedLocationProviderClient!!.lastLocation

        task?.addOnSuccessListener { location ->

            if (location != null) {
                this.currentLocation = location
                Toast.makeText(
                    this,
                    "lat:${location.altitude}, lon${location.longitude}",
                    Toast.LENGTH_LONG
                ).show()

                val mapFragment = supportFragmentManager
                    .findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this@MapsActivity)

            }
        }

    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {

            1000 -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(
                        this, android.Manifest.permission.ACCESS_FINE_LOCATION
                    )
                    == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this, android.Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                    == PackageManager.PERMISSION_GRANTED
                ) {

                    fetchLocation()
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        drawMarker(LatLng(currentLocation!!.latitude, currentLocation!!.longitude))

        mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker) {

            }

            override fun onMarkerDrag(marker: Marker) {

            }

            override fun onMarkerDragEnd(marker: Marker) {

                if (currentMarker != null) {
                    currentMarker?.remove()

                    val latLon = LatLng(marker.position.latitude, marker.position.longitude)

                    Toast.makeText(
                        applicationContext,
                        "lat:${latLon.latitude}, lon${latLon.longitude}",
                        Toast.LENGTH_LONG
                    ).show()

                    drawMarker(latLon)
                }
            }

        })
    }

    private fun drawMarker(latLng: LatLng) {

        val markerOptions = MarkerOptions().position(latLng).title("I am here").draggable(true)
//            .snippet(getAddress(latLng.latitude, latLng.longitude)).draggable(true)

        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f))
        currentMarker = mMap.addMarker(markerOptions)
        currentMarker?.showInfoWindow()

    }

    private fun getAddress(lat: Double, lon: Double): String {

        val geoCeder = Geocoder(this)
        val address = geoCeder.getFromLocation(lat, lon, 1)
        return address[0].getAddressLine(0)

//        try {
//
//            val geocoder = Geocoder(this, Locale.getDefault())
//            val addresses: List<Address> = geocoder.getFromLocation(lat, lon, 1)
//
//            address = addresses[0].getAddressLine(0).toString()
//            val city: String = addresses[0].getLocality()
//            val state: String = addresses[0].getAdminArea()
//            val country: String = addresses[0].getCountryName()
//            val postalCode: String = addresses[0].getPostalCode()
//            val knownName: String = addresses[0].getFeatureName()
//
//        } catch (e: Exception) {
//
//
//        }
//        return address!!
    }

}