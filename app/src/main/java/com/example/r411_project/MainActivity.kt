package com.example.r411_project

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import android.app.AlertDialog
import android.content.Intent

class MainActivity : AppCompatActivity() {
    lateinit var capteurManager: CapteurManager
    lateinit var enemyCarManager: EnemyCarManager
    lateinit var scoreManager: ScoreManager
    lateinit var ivCar: ImageView
    lateinit var mainLayout: ConstraintLayout
    lateinit var btnClose: ImageView

    private var engineMediaPlayer: MediaPlayer? = null

    private lateinit var preferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialiser les préférences partagées
        preferences = getSharedPreferences("Game", Context.MODE_PRIVATE)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialiser le bouton de fermeture
        btnClose = findViewById(R.id.btnClose)
        btnClose.setOnClickListener {
            showCloseConfirmationDialog()
        }


        // Initialiser les vues et les gestionnaires
        mainLayout = findViewById(R.id.main)
        capteurManager = CapteurManager(this)
        capteurManager.start()
        ivCar = findViewById(R.id.ivCar)

        // Initialiser et démarrer le son du moteur en fonction des préférences
        val engineSoundEnabled = preferences.getBoolean("ENGINE_SOUND", true)
        if (engineSoundEnabled) {
            setupEngineSound()
        }

        // Initialiser le ScoreManager
        val tvScore = findViewById<TextView>(R.id.tvScore)
        scoreManager = ScoreManager(tvScore)

        // Initialiser et démarrer le gestionnaire de voitures ennemies
        enemyCarManager = EnemyCarManager(this, mainLayout, ivCar, scoreManager)
        enemyCarManager.start()

        // Démarrer le comptage du score
        scoreManager.startScoring()
    }

    // Méthode pour afficher une boîte de dialogue de confirmation

    private fun showCloseConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Quitter le jeu")
            .setMessage("Voulez-vous vraiment quitter ? Votre score ne sera pas sauvegardé.")
            .setPositiveButton("Oui") { _, _ ->
                // Arrêter tous les processus sans sauvegarder
                enemyCarManager.stop()
                scoreManager.stopScoring()
                capteurManager.stop()

                // Retourner à l'écran d'accueil
                val intent = Intent(this, AccueilActivity::class.java)
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Non", null)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Libérer les ressources audio
        engineMediaPlayer?.apply {
            if (isPlaying) { stop() }
            release()
        }
        enemyCarManager.stop()
        scoreManager.stopScoring()
        capteurManager.stop()
    }

    private fun setupEngineSound() {
        // Créer le MediaPlayer pour le son de moteur
        engineMediaPlayer = MediaPlayer.create(this, R.raw.car_engine).apply {
            isLooping = true
            //setVolume(0.3f, 0.3f) // Volume réduit
        }
        engineMediaPlayer?.start()
    }

    override fun onPause() {
        super.onPause()
        // Mettre en pause le son du moteur
        engineMediaPlayer?.pause()
    }

    override fun onResume() {
        super.onResume()
        // Reprendre le son du moteur
        engineMediaPlayer?.start()
    }
}