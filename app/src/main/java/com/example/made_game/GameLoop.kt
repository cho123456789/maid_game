package com.example.made_game

import android.graphics.Canvas
import android.view.SurfaceHolder

class GameLoop(
    private val surfaceHolder: SurfaceHolder,
    private val gameView: GameView
) : Thread() {

    @Volatile
    private var running = false

    fun startLoop() {
        running = true
        start()
    }

    fun stopLoop() {
        running = false
        join()
    }

    override fun run() {

        var lastTime = System.currentTimeMillis()

        while (running) {

            val now = System.currentTimeMillis()
            val delta = now - lastTime
            lastTime = now

            gameView.update(delta)

            var canvas: Canvas? = null

            try {

                canvas = surfaceHolder.lockCanvas()

                synchronized(surfaceHolder) {

                    canvas?.let {
                        gameView.render(it)
                    }
                }

            } finally {

                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas)
                }
            }

            // 60FPS 유지
            val frameTime = System.currentTimeMillis() - now

            if (frameTime < 16) {
                sleep(16 - frameTime)
            }
        }
    }
}