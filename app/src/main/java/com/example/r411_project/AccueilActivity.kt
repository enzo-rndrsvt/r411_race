package com.example.r411_project

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class AccueilActivity : AppCompatActivity() {
    lateinit var btnStartGame: Button
    lateinit var btnSettings: Button
    lateinit var btnHowToPlay: Button
    lateinit var tvBestScore: TextView
    private lateinit var preferences: SharedPreferences
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_accueil)

        // Initialiser les préférences partagées pour sauvegarder le meilleur score
        preferences = getSharedPreferences("GamePrefs", Context.MODE_PRIVATE)

        // Bouton pour commencer le jeu
        btnStartGame = findViewById(R.id.btnStartGame)
        btnStartGame.setOnClickListener {
            openMainActivity()
        }

        // Bouton pour ouvrir les paramètres
        btnSettings = findViewById(R.id.btnSettings)
        btnSettings.setOnClickListener {
            openSettingsActivity()
        }

        // Bouton pour ouvrir la page de comment jouer
        btnHowToPlay = findViewById(R.id.btnHowToPlay)
        btnHowToPlay.setOnClickListener {
            openHowToPlayActivity()
        }

        // Récupérer le meilleur score depuis les préférences partagées
        score = preferences.getInt("BEST_SCORE", 0)

        // Afficher le meilleur score
        tvBestScore = findViewById(R.id.tvBestScore)
        tvBestScore.text = "Meilleur score: $score"
    }

    // Fonction pour ouvrir l'activité des paramètres
    private fun openSettingsActivity() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    // Fonction pour ouvrir l'activité principale (jeu)
    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    // Fonction pour ouvrir l'activité de comment jouer
    private fun openHowToPlayActivity() {
        val intent = Intent(this, HowToPlayActivity::class.java)
        startActivity(intent)
    }
}