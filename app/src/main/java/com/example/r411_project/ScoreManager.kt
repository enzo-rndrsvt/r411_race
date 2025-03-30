package com.example.r411_project

import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.preference.PreferenceManager

class ScoreManager(private val scoreTextView: TextView) {
    private var currentScore = 0
    private val handler = Handler(Looper.getMainLooper())
    private val scoreIncrementRunnable = object : Runnable {
        override fun run() {
            incrementScore()
            // Répéter toutes les secondes
            handler.postDelayed(this, 1000)
        }
    }

    // Commencer l'incrémentation du score
    fun startScoring() {
        handler.post(scoreIncrementRunnable)
    }

    // Arrêter l'incrémentation du score
    fun stopScoring() {
        handler.removeCallbacks(scoreIncrementRunnable)
    }

    // Incrémenter le score
    private fun incrementScore() {
        currentScore++
        updateScoreDisplay()
    }

    // Mettre à jour l'affichage du score
    private fun updateScoreDisplay() {
        scoreTextView.text = "Score: $currentScore"
    }

    // Réinitialiser le score
    fun resetScore() {
        currentScore = 0
        updateScoreDisplay()
    }

    // Obtenir le score actuel
    fun getScore(): Int = currentScore
}