package com.example.my_mountain.ui.location

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.my_mountain.MainActivity
import com.example.my_mountain.R
import com.example.my_mountain.databinding.FragmentLocationBinding

class LocationFragment : Fragment(), SensorEventListener {

    lateinit var binding: FragmentLocationBinding
    lateinit var mainActivity: MainActivity

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var magnetometer: Sensor? = null

    private lateinit var compassImageView: ImageView

    private var gravity = FloatArray(3)
    private var geomagnetic = FloatArray(3)

    // 몇 초간 데이터를 수집하여 초기화하는 코드 추가
    private var calibrationCount = 0
    private val maxCalibrationCount = 50 // 대략 5초간의 데이터 수집 (센서 업데이트 속도에 따라 다를 수 있음)


    override fun onAttach(context: Context) {
        super.onAttach(context)
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentLocationBinding.inflate(layoutInflater)
        compassImageView = binding.imageLocation
        mainActivity = activity as MainActivity
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        accelerometer?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME) // 빠른 업데이트 속도 사용
        }
        magnetometer?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> gravity = event.values.clone()
            Sensor.TYPE_MAGNETIC_FIELD -> geomagnetic = event.values.clone()
        }

        if (calibrationCount < maxCalibrationCount) {
            // 첫 5초 동안의 데이터를 수집하여 초기화
            calibrationCount++
        } else {
            if (gravity.isNotEmpty() && geomagnetic.isNotEmpty()) {
                val R = FloatArray(9)
                val I = FloatArray(9)
                val success = SensorManager.getRotationMatrix(R, I, gravity, geomagnetic)
                if (success) {
                    val orientation = FloatArray(3)
                    SensorManager.getOrientation(R, orientation)
                    var azimuthInDegrees = Math.toDegrees(orientation[0].toDouble()).toFloat()

                    if (azimuthInDegrees < 0) {
                        azimuthInDegrees += 360
                    }

                    compassImageView.rotation = -azimuthInDegrees

                    val direction = when {
                        azimuthInDegrees >= 337.5 || azimuthInDegrees < 22.5 -> "북"
                        azimuthInDegrees >= 22.5 && azimuthInDegrees < 67.5 -> "북동"
                        azimuthInDegrees >= 67.5 && azimuthInDegrees < 112.5 -> "동"
                        azimuthInDegrees >= 112.5 && azimuthInDegrees < 157.5 -> "남동"
                        azimuthInDegrees >= 157.5 && azimuthInDegrees < 202.5 -> "남"
                        azimuthInDegrees >= 202.5 && azimuthInDegrees < 247.5 -> "남서"
                        azimuthInDegrees >= 247.5 && azimuthInDegrees < 292.5 -> "서"
                        else -> "북서"
                    }

                    binding.textCurrentLocation.text = "현재 방향: ${azimuthInDegrees.toInt()}° ($direction)"
                }
            }
        }
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}