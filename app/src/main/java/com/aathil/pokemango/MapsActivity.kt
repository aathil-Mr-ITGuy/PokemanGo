package com.aathil.pokemango

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        checkPermission()
        LoadPokeMan()
    }

    var ACCESSLOCATION = 123
    fun checkPermission(){
        if(Build.VERSION.SDK_INT > 23){
            if(ActivityCompat.
                    checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), ACCESSLOCATION)
                return


            }
        }
        getUserLocation()
    }

    fun getUserLocation(){
        Toast.makeText(this, "User's location Access on", Toast.LENGTH_LONG).show()
        //TODO: will implement later
        var myLocation = MyLocationListener()
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3,3f,myLocation)
        var myThread = myThread()
        myThread.start()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            ACCESSLOCATION -> {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getUserLocation()
                }else{
                    Toast.makeText(this, "We cannot access you location", Toast.LENGTH_LONG ).show()
                }
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera

    }

    var location : Location? = null
    //get User Location
    inner class MyLocationListener:LocationListener{

        constructor(){
            location = Location("Start")
            location!!.longitude = 0.0
            location!!.longitude = 0.0
        }
        override fun onLocationChanged(p0: Location) {
            location = p0
            //TODO("Not yet implemented")
        }

        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
            //super.onStatusChanged(provider, status, extras)
        }

        override fun onProviderEnabled(p0: String) {
            //super.onProviderEnabled(provider)
        }

        override fun onProviderDisabled(p0: String) {
            //super.onProviderDisabled(provider)
        }


    }

    var oldLocation : Location? = null
    inner class myThread:Thread{
        constructor():super(){
            oldLocation= Location("Start")
            oldLocation!!.longitude=0.0
            oldLocation!!.longitude=0.0
        }

        override fun run() {
            while(true){
                try {
                    if(oldLocation!!.distanceTo(location)==0f){
                        continue
                    }

                    oldLocation=location

                    runOnUiThread{
                        mMap!!.clear()
                        val sydney = LatLng(location!!.latitude, location!!.longitude)
                        mMap.addMarker(MarkerOptions()
                                .position(sydney)
                                .title("Me")
                                .snippet("Here is my location")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mario))
                        )
                        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,14f))

                        for(i in 0 until listPokeman.size){
                            var newPokeman = listPokeman[i]
                            if(newPokeman.isCatch == false){
                                val pokemanlog = LatLng(newPokeman.location!!.latitude, newPokeman.location!!.longitude)
                                mMap.addMarker(MarkerOptions()
                                    .position(pokemanlog)
                                        .title(newPokeman.name!!)
                                        .snippet(newPokeman.des!! + ", power:"  + newPokeman!!.power)
                                        .icon(BitmapDescriptorFactory.fromResource(newPokeman.image!!))

                                )

                                if(location!!.distanceTo(newPokeman.location)<2){
                                    newPokeman.isCatch = true
                                    listPokeman[i] = newPokeman
                                    playerPower += newPokeman.power!!
                                    Toast.makeText(applicationContext, "You hav cathced new poeman, your power is " + playerPower, Toast.LENGTH_LONG).show()
                                }
                            }
                        }

                    }
                    Thread.sleep(1000)


                }catch (ex: Exception){}
            }
        }
    }

    var playerPower = 0.0
    var listPokeman = ArrayList<Pokeman>()
    fun LoadPokeMan(){
        //charmander
        listPokeman.add(Pokeman("Charmander","I am from Japan", R.drawable.charmander, 55.0 ,6.9241, 79.8620 ))
        //Bulbasaur
        listPokeman.add(Pokeman("Bulbasaur","I am from Sri Lanka", R.drawable.bulbasaur, 3.0 ,45.7843, -56.568 ))
        //squirtle
        listPokeman.add(Pokeman("Squirtle","I am from Sri Lanka", R.drawable.squirtle, 3.0 ,45.7843, -56.568 ))
    }
}