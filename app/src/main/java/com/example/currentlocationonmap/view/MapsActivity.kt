package com.example.currentlocationonmap.view

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.currentlocationonmap.R
import com.example.currentlocationonmap.databinding.ActivityMapsBinding
import com.example.currentlocationonmap.model.MapModel
import com.example.currentlocationonmap.utils.Constants.Companion.REQUEST_CODE
import com.example.currentlocationonmap.viewModel.MapViewModel
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

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val viewModel: MapViewModel? = null

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

    //fetchLocation
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
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE
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

            REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

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

                    val latLng = LatLng(marker.position.latitude, marker.position.longitude)

                    //add marker
                    drawMarker(latLng)

                    //insertLatLog into room
                    insertLatLon(latLng)

                }
            }

        })
    }

    //drawMarker
    private fun drawMarker(latLong: LatLng) {

        val markerOptions = MarkerOptions().position(latLong).title("I am here").draggable(true)

        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLong))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLong, 14f))
        currentMarker = mMap.addMarker(markerOptions)
        currentMarker?.showInfoWindow()

    }

    //insert latitude and latitude in room
    private fun insertLatLon(latLong: LatLng){

            val mapModel = MapModel(0, latLong.latitude, latLong.longitude)
            viewModel?.insertLatLon(mapModel)

        Toast.makeText(
            applicationContext,
            "latitude:${latLong.latitude}, longitude${latLong.longitude} saved", Toast.LENGTH_LONG).show()

    }

}