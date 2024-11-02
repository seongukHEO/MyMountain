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

    private var currentDegree = 0f
    private lateinit var compassImageView: ImageView

    private var gravity = FloatArray(3)
    private var geomagnetic = FloatArray(3)

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
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
        magnetometer?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event: SensorEvent) {
        when(event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> gravity = event.values.clone()
            Sensor.TYPE_MAGNETIC_FIELD -> geomagnetic = event.values.clone()
        }

        if (gravity.isNotEmpty() && geomagnetic.isNotEmpty()) {
            val R = FloatArray(9)
            val I = FloatArray(9)
            val success = SensorManager.getRotationMatrix(R, I, gravity, geomagnetic)
            if (success) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(R, orientation)
                val azimuthInRadians = orientation[0]
                var azimuthInDegrees = Math.toDegrees(azimuthInRadians.toDouble()).toFloat()

                // 음수일 경우 360을 더해 양수로 변환
                if (azimuthInDegrees < 0) {
                    azimuthInDegrees += 360
                }

                // 방향 회전 애니메이션
                compassImageView.rotation = -azimuthInDegrees

                // 현재 각도에 따른 방향 텍스트 설정
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

                // 방향과 각도를 TextView에 표시
                binding.textCurrentLocation.text = "현재 방향: ${azimuthInDegrees.toInt()}° ($direction)"
            }
        }
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}