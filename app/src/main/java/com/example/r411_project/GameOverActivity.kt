package com.example.r411_project

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameOverActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)

        // Récupérer le score actuel et le meilleur score depuis l'intent
        val currentScore = intent.getIntExtra("FINAL_SCORE", 0)
        val bestScore = intent.getIntExtra("BEST_SCORE", 0)

        // Trouver les TextViews pour afficher les scores
        val tvFinalScore = findViewById<TextView>(R.id.tvFinalScore)
        val tvBestScore = findViewById<TextView>(R.id.tvBestScore)

        // Afficher le score actuel
        tvFinalScore.text = "Score: $currentScore"

        // Vérifier si le score actuel est le meilleur score
        if (currentScore > bestScore) {
            tvBestScore.text = "Nouveau meilleur score: $currentScore"
            tvBestScore.setTextColor(Color.GREEN) // Changer la couleur du texte en vert
        } else {
            tvBestScore.text = "Meilleur Score: $bestScore"
        }

        // Bouton pour redémarrer le jeu
        val btnRestart = findViewById<Button>(R.id.btnRestart)
        btnRestart.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Bouton pour retourner au menu principal
        val btnMainMenu = findViewById<Button>(R.id.btnMainMenu)
        btnMainMenu.setOnClickListener {
            val intent = Intent(this, AccueilActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}