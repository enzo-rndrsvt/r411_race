package com.example.r411_project

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Rect
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ViewTreeObserver
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.random.Random

class EnemyCarManager(
    private val context: MainActivity,
    private val mainLayout: ConstraintLayout,
    private val playerCar: ImageView,
    private val scoreManager: ScoreManager
)
{
    private var crashSoundPlayer: MediaPlayer? = null
    private val enemyCars = mutableListOf<ImageView>()
    private val handler = Handler(Looper.getMainLooper())
    private var isGameOver = false

    init {
        // Initialiser le lecteur de son pour le crash
        crashSoundPlayer = MediaPlayer.create(context, R.raw.car_crash)
        crashSoundPlayer?.isLooping = false
    }

    // Récuperer les préférences de l'application
    private val preferences: SharedPreferences = context.getSharedPreferences("Game", Context.MODE_PRIVATE)

    // Runnable pour vérifier les collisions
    private val collisionCheckRunnable = object : Runnable {
        override fun run() {
            if (!isGameOver) {
                checkCollisions()
                handler.postDelayed(this, 50)
            }
        }
    }

    // Fonction pour vérifier les collisions entre la voiture du joueur et les voitures ennemies
    private fun checkCollisions() {
        val playerRect = Rect()
        playerCar.getHitRect(playerRect)

        for (enemyCar in enemyCars.toList()) {
            val enemyRect = Rect()
            enemyCar.getHitRect(enemyRect)

            if (Rect.intersects(playerRect, enemyRect)) {
                handleGameOver()
                return
            }
        }
    }

    // Fonction pour gérer la fin du jeu
    private fun handleGameOver() {
        if (isGameOver) return
        isGameOver = true

        // Jouer le son de crash si activé dans les paramètres
        val engineSoundEnabled = preferences.getBoolean("CRASH_SOUND", true)
        if (engineSoundEnabled){
            crashSoundPlayer?.start()
        }

        // Arrêter tous les processus
        handler.removeCallbacksAndMessages(null)
        scoreManager.stopScoring()

        // Désactiver le capteur
        context.capteurManager.stop()

        // Récupérer le meilleur score actuel
        val currentScore = scoreManager.getScore()
        val bestScore = preferences.getInt("BEST_SCORE", 0)

        // Mettre à jour le meilleur score si nécessaire
        if (currentScore > bestScore) {
            preferences.edit().putInt("BEST_SCORE", currentScore).apply()
        }

        // Lancer l'activité Game Over
        context.runOnUiThread {
            val intent = Intent(context, GameOverActivity::class.java).apply {
                putExtra("FINAL_SCORE", currentScore)
                putExtra("BEST_SCORE", bestScore)
            }
            context.startActivity(intent)
            context.finish()
        }
    }

    // Runnable pour générer des voitures ennemies
    private val enemyCarSpawnRunnable = object : Runnable {
        override fun run() {
            if (!isGameOver) {
                // Générer des voitures ennemies
                repeat(Random.nextInt(1, 2)) {
                    spawnEnemyCar()
                }
                // Délai entre les spawns
                handler.postDelayed(this, Random.nextLong(500, 1500))
            }
        }
    }

    // Libérer les ressources du lecteur de son
    fun release() {
        crashSoundPlayer?.release()
        crashSoundPlayer = null
    }

    // Démarrer la génération de voitures ennemies et la vérification des collisions
    fun start() {
        isGameOver = false
        enemyCars.clear()
        handler.post(enemyCarSpawnRunnable)
        handler.post(collisionCheckRunnable)
    }

    // Arrêter la génération de voitures ennemies et la vérification des collisions
    fun stop() {
        isGameOver = true
        handler.removeCallbacksAndMessages(null)
        enemyCars.clear()
        release()
    }

    // Fonction pour générer une voiture ennemie
    private fun spawnEnemyCar() {
        if (isGameOver) return

        val route = Random.nextInt(3)
        val enemyCar = ImageView(context).apply {
            setImageResource(R.drawable.carenemy)
            layoutParams = createLayoutParamsForRoute(route)
            translationY = -playerCar.height.toFloat()
        }

        mainLayout.addView(enemyCar)
        enemyCars.add(enemyCar)

        animateEnemyCar(enemyCar)
    }

    // Créer les paramètres de layout pour la route spécifiée
    private fun createLayoutParamsForRoute(route: Int): ConstraintLayout.LayoutParams {
        return ConstraintLayout.LayoutParams(playerCar.width, playerCar.height).apply {
            when (route) {
                0 -> { // Route de gauche
                    startToStart = R.id.vCoteG
                    endToStart = R.id.guideline
                }
                1 -> { // Route du milieu
                    startToEnd = R.id.guideline
                    endToStart = R.id.guideline3
                }
                2 -> { // Route de droite
                    startToEnd = R.id.guideline3
                    endToStart = R.id.vCoteD
                }
            }
            topToTop = R.id.main
            bottomToTop = R.id.main
        }
    }

    // Animer la voiture ennemie pour qu'elle descende l'écran
    private fun animateEnemyCar(enemyCar: ImageView) {
        enemyCar.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                enemyCar.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val screenHeight = mainLayout.height
                val carHeight = enemyCar.height

                enemyCar.animate()
                    .translationY(screenHeight.toFloat() + carHeight)
                    .setDuration(3000)
                    .setInterpolator(LinearInterpolator())
                    .withEndAction {
                        mainLayout.removeView(enemyCar)
                        enemyCars.remove(enemyCar)
                    }
            }
        })
    }
}