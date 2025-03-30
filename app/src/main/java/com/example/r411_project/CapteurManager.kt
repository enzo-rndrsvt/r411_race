package com.example.r411_project

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.View
import androidx.appcompat.app.AlertDialog

class CapteurManager (pContext: MainActivity) : SensorEventListener {

    private val context = pContext
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    // Afficher une fenêtre d'alerte si le capteur nécessaire n'est pas disponible
    private fun windowPasCapteurNecessaire() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder
            .setTitle("Terminal incompatible")
            .setMessage("Cette application a besoin d'un capteur TYPE_ACCELEROMETER pour fonctionner et votre terminal " +
                    "n'offre pas ce capteur. \nDésolé, nous allons devoir fermer l'application.")
            .setPositiveButton("OK") {dialog, which ->
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_HOME)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        val dialog: AlertDialog = builder.create()

        dialog.show()
    }

    // Gérer le déplacement de la voiture à l'aide du capteur
    override fun onSensorChanged(event: SensorEvent?) {
        if(event?.sensor?.type == Sensor.TYPE_ACCELEROMETER){
            val facteur= 10
            val posX = context.ivCar.x - event.values[0] * facteur
            limiterPosition(posX)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    // Fonction pour éviter que la voiture ne dépasse les bordures
    private fun limiterPosition(posX: Float){
        val car = context.ivCar
        val screenWidth = context.resources.displayMetrics.widthPixels
        val carWith = car.width

        val bordureGauche = context.findViewById<View>(R.id.vCoteG)
        val bordureDroite = context.findViewById<View>(R.id.vCoteD)

        val minX = bordureGauche.width.toFloat()
        val maxX = (screenWidth - bordureDroite.width - carWith).toFloat()

        car.x = posX.coerceIn(minX, maxX)
    }

    // Démarrer la détection du capteur
    fun start(){
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME)
    }

    // Reprendre la détection du capteur
    fun resume(){
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME)
    }

    // Arrêter la détection du capteur
    fun stop(){
        sensorManager.unregisterListener(this)
    }

    // Mettre en pause la détection du capteur
    fun pause(){
        sensorManager.unregisterListener(this)
    }
}