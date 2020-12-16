package com.mmoson.waypoint.UI

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mmoson.waypoint.R
import com.mmoson.waypoint.services.LocationService


class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController
    lateinit var thisView: View
    lateinit var context: Context
    lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        thisView = findViewById(R.id.nav_host_frag)
        context = this.applicationContext
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val permission1 = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        ActivityCompat.requestPermissions(this, permission1, 1)
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_frag) as NavHostFragment? ?: return
        navController = host.navController
        toolbar.setupWithNavController(navController)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val service = Intent(this, LocationService::class.java)
        startService(service)
    }
}