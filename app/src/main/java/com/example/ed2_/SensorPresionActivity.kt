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
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SensorPresionActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var tvTitulo: TextView
    private lateinit var tvValores: TextView
    private lateinit var tvEstado: TextView
    private lateinit var btnContadores: Button
    private lateinit var layoutPrincipal: LinearLayout

    private lateinit var sensorManager: SensorManager
    private var sensorPresion: Sensor? = null

    private var contadorBaja = 0
    private var contadorNormal = 0
    private var contadorAlta = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_presion)

        tvTitulo = findViewById(R.id.tvTitulo)
        tvValores = findViewById(R.id.vtValores)
        tvEstado = findViewById(R.id.tvEstado)
        btnContadores = findViewById(R.id.btnContadores)
        layoutPrincipal = findViewById(R.id.layoutPrincipal)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensorPresion = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)

        if (sensorPresion == null) {
            tvEstado.text = "El sensor de presión no está disponible en este dispositivo"
        }

        btnContadores.setOnClickListener {
            val mensaje = "Presión baja: $contadorBaja\n" +
                    "Presión normal: $contadorNormal\n" +
                    "Presión alta: $contadorAlta"
            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_PRESSURE) {
            val presion = event.values[0]
            tvValores.text = "Presión: $presion hPa"

            if (presion < 1000) {
                presionBaja()
            } else if (presion <= 1020) {
                presionNormal()
            } else {
                presionAlta()
            }
        }
    }

    private fun presionBaja() {
        contadorBaja++
        tvEstado.text = "Presión baja"
        layoutPrincipal.setBackgroundColor(Color.BLUE)
        tvTitulo.setTextColor(Color.WHITE)
        tvEstado.setTextColor(Color.WHITE)
        tvValores.setTextColor(Color.WHITE)
    }

    private fun presionNormal() {
        contadorNormal++
        tvEstado.text = "Presión normal"
        layoutPrincipal.setBackgroundColor(Color.GREEN)
        tvTitulo.setTextColor(Color.BLACK)
        tvEstado.setTextColor(Color.BLACK)
        tvValores.setTextColor(Color.BLACK)
    }

    private fun presionAlta() {
        contadorAlta++
        tvEstado.text = "Presión alta"
        layoutPrincipal.setBackgroundColor(Color.YELLOW)
        tvTitulo.setTextColor(Color.BLACK)
        tvEstado.setTextColor(Color.BLACK)
        tvValores.setTextColor(Color.BLACK)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onResume() {
        super.onResume()
        sensorPresion?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}