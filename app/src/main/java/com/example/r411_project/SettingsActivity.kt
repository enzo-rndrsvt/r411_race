package com.example.r411_project

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Initialiser SharedPreferences
        sharedPreferences = getSharedPreferences("Game", Context.MODE_PRIVATE)

        // Sons
        val switchEngineSound = findViewById<Switch>(R.id.switchEngineSound)
        val switchCrashSound = findViewById<Switch>(R.id.switchCrashSound)

        // Charger les paramètres existants
        switchEngineSound.isChecked = sharedPreferences.getBoolean("ENGINE_SOUND", true)
        switchCrashSound.isChecked = sharedPreferences.getBoolean("CRASH_SOUND", true)

        // Bouton Enregistrer
        val btnSaveSettings = findViewById<Button>(R.id.btnSaveSettings)
        btnSaveSettings.setOnClickListener {
            saveSettings(
                switchEngineSound.isChecked,
                switchCrashSound.isChecked,
            )
        }
    }

    // Fonction pour sauvegarder les paramètres
    private fun saveSettings(
        engineSound: Boolean,
        crashSound: Boolean,
    ) {
        sharedPreferences.edit().apply {
            putBoolean("ENGINE_SOUND", engineSound)
            putBoolean("CRASH_SOUND", crashSound)
        }.apply()

        // Retourner à l'écran précédent
        finish()
    }
}