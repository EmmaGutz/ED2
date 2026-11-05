package com.example.ed2_

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SensorAcelerometroActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var tvModo: TextView
    private lateinit var tvEjeX: TextView
    private lateinit var tvEjeY: TextView
    private lateinit var tvEjeZ: TextView
    private lateinit var layout: LinearLayout
    private lateinit var tvSensor: TextView
    private lateinit var btnContador: Button
    private lateinit var sensorManager: SensorManager
    private var sensorAce: Sensor? = null
    private var contadorNorm = 0
    private var contadorBrus = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acelerometro)
        tvModo = findViewById(R.id.tvModo)
        tvEjeX = findViewById(R.id.tvEjeX)
        tvEjeY = findViewById(R.id.tvEjeY)
        tvEjeZ = findViewById(R.id.tvEjeZ)
        tvSensor = findViewById(R.id.tvSensor)
        layout = findViewById(R.id.layout)
        btnContador = findViewById(R.id.btnContador)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensorAce = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (sensorAce == null) {
            tvModo.text = "No hay Acelerometro"
        }
        btnContador.setOnClickListener {
            Toast.makeText(this, "Contador brusco: $contadorBrus, Contador normal: $contadorNorm", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val ejeX = event.values[0]
            val ejeY = event.values[1]
            val ejeZ = event.values[2]
            tvEjeX.text = "EJE X: $ejeX"
            tvEjeY.text = "EJE Y: $ejeY"
            tvEjeZ.text = "EJE Z: $ejeZ"
            if (ejeX > 12 && ejeY > 12 && ejeZ > 12) {
                layout.setBackgroundColor(Color.RED)
                tvModo.setTextColor(Color.WHITE)
                tvModo.text = "Movimiento brusco detectado"
                contadorBrus++
                tvSensor.setTextColor(Color.WHITE)
                tvEjeX.setTextColor(Color.WHITE)
                tvEjeY.setTextColor(Color.WHITE)
                tvEjeZ.setTextColor(Color.WHITE)
            } else if (ejeX < 12 && ejeY < 12 && ejeZ < 12) {
                layout.setBackgroundColor(Color.GREEN)
                tvModo.setTextColor(Color.BLACK)
                tvModo.text = "Movimiento Normal"
                contadorNorm++
                tvSensor.setTextColor(Color.BLACK)
                tvEjeX.setTextColor(Color.BLACK)
                tvEjeY.setTextColor(Color.BLACK)
                tvEjeZ.setTextColor(Color.BLACK)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onResume() {
        super.onResume()
        sensorAce?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}
