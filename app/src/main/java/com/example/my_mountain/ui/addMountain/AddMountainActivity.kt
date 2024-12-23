package com.example.my_mountain.ui.addMountain

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.my_mountain.R
import com.example.my_mountain.databinding.ActivityAddMountainBinding
import com.example.my_mountain.service.LocationService
import com.example.my_mountain.service.StopwatchService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions



class AddMountainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMountainBinding

    private lateinit var locationManager: LocationManager

    private lateinit var mainGoogleMap: GoogleMap
    private var pathPoints = mutableListOf<LatLng>()
    private lateinit var polyline: Polyline
    private val polylineOptions = PolylineOptions().color(Color.GREEN).width(10f)

    private val locationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "LOCATION_UPDATE") {
                val location = intent.getParcelableExtra<Location>("location")
                location?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    pathPoints.add(latLng)
                    polyline.points = pathPoints
                    Log.d("PolylineUpdate", "Polyline updated with points: $pathPoints")
                }
            }
        }
    }

    private var running = false

    private fun startStopwatchService() {
        val intent = Intent(this, StopwatchService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)  // Android 8.0 이상에서 사용
        } else {
            startService(intent)
        }
    }

    private val updateTime = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "STOPWATCH_UPDATE"){
                val time = intent.getStringExtra("time")
                if (time != null) {
                    updateStopwatchText(time)
                }
            }
        }

    }

    private fun stopStopwatchService(){
        val intent = Intent(this, StopwatchService::class.java)
        stopService(intent)
    }


    // 위치 측정이 성공하면 동작할 리스너
    var gpsLocationListener: MyLocationListener? = null
    var networkLocationListener: MyLocationListener? = null

    private val permissionList = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.FOREGROUND_SERVICE // 추가
    )


    private val REQUEST_LOCATION_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapsInitializer.initialize(this, MapsInitializer.Renderer.LATEST, null)

        binding = ActivityAddMountainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        manageButton()

        LocalBroadcastManager.getInstance(this).registerReceiver(
            updateTime,
            IntentFilter("STOPWATCH_UPDATE")
        )

        LocalBroadcastManager.getInstance(this).registerReceiver(
            locationReceiver,
            IntentFilter("LOCATION_UPDATE")
        )

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        if (hasLocationPermissions()) {
            settingGoogleMap()
        } else {
            ActivityCompat.requestPermissions(this, permissionList, REQUEST_LOCATION_PERMISSION)
        }
    }

    private fun updateStopwatchText(time: String){
        binding.apply {
            stopWatchTextView.text = time
        }
    }

    // 권한이 이미 부여되었는지 확인
    private fun hasLocationPermissions(): Boolean {
        return permissionList.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    //버튼 관리
    private fun manageButton(){
        binding.apply {
            buttonMountainStart.setOnClickListener {
                if (!running){
                    startStopwatchService()
                    startTrackingLocation()
                    running = true
                }
            }
            buttonMountainEnd.setOnClickListener {
                if (running){
                    stopStopwatchService()
                    stopTrackingLocation()
                    running = false
                }
            }
        }
    }

    // 권한 요청 결과 처리
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                settingGoogleMap()
            } else {
                // 권한이 거부되었을 때의 처리
                // 예: 사용자에게 권한이 필요함을 알리는 메시지 표시
            }
        }
    }

    inner class MyLocationListener : LocationListener {
        override fun onLocationChanged(location: Location) {
            when (location.provider) {
                LocationManager.GPS_PROVIDER -> {
                    gpsLocationListener?.let {
                        locationManager.removeUpdates(it)
                        gpsLocationListener = null
                    }
                }
                LocationManager.NETWORK_PROVIDER -> {
                    networkLocationListener?.let {
                        locationManager.removeUpdates(it)
                        networkLocationListener = null
                    }
                }
            }
            setMyLocation(location)
        }
    }


    private fun setMyLocation(location: Location) {
        val userLocation = LatLng(location.latitude, location.longitude)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(userLocation, 15.0f)
        mainGoogleMap.animateCamera(cameraUpdate)
    }

    private fun settingGoogleMap() {
        val supportMapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment

        // 구글 지도 사용이 완료되면 동작
        supportMapFragment.getMapAsync { googleMap ->
            mainGoogleMap = googleMap

            // Polyline을 GoogleMap에 추가
            polyline = mainGoogleMap.addPolyline(polylineOptions)

            // 지도 설정 완료 후 위치 설정
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            ) {
                mainGoogleMap.isMyLocationEnabled = true
                mainGoogleMap.uiSettings.isMyLocationButtonEnabled = true
                getLastKnownLocation()
                getMyLocation()
            }

            mainGoogleMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
        }
    }


    private fun getLastKnownLocation() {
        val gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        if (gpsLocation != null) {
            setMyLocation(gpsLocation)
        } else if (networkLocation != null) {
            setMyLocation(networkLocation)
        }
    }

    private fun getMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED
        ) {
            return
        }

        // GPS가 사용 가능하다면
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (gpsLocationListener == null) {
                gpsLocationListener = MyLocationListener()
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    0.0f,
                    gpsLocationListener!!
                )
                Log.d("AddMountainActivity", "GPS LocationListener initialized")
            }
        }

        // Network가 사용 가능하다면
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (networkLocationListener == null) {
                networkLocationListener = MyLocationListener()
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0,
                    0.0f,
                    networkLocationListener!!
                )
                Log.d("AddMountainActivity", "Network LocationListener initialized")
            }
        }
    }


    //polyLine
    private fun startTrackingLocation(){
        val intent = Intent(this, LocationService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            startForegroundService(intent)
        }else{
            startService(intent)
        }
    }

    private fun stopTrackingLocation(){
        val intent = Intent(this, LocationService::class.java)
        stopService(intent)
    }


}
