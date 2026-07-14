package com.example.made_game

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class GameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {

    private lateinit var gameLoop: GameLoop
    private lateinit var background: Bitmap
    private lateinit var maids: List<MovingMaid>
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

    init {
        holder.addCallback(this)
        isFocusable = true
    }

    override fun surfaceCreated(holder: SurfaceHolder) {

        background = BitmapFactory.decodeResource(resources, R.drawable.maid_background)

        maids = listOf(
            createMaid(R.drawable.maid_test, 80f, 100f),
            createMaid(R.drawable.orange_maid, 360f, 180f),
            createMaid(R.drawable.sister_maid, 140f, 520f),
            createMaid(R.drawable.yellow_maid, 520f, 620f)
        )

        gameLoop = GameLoop(holder, this)
        gameLoop.startLoop()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {

        gameLoop.stopLoop()
    }

    override fun surfaceChanged(
        holder: SurfaceHolder,
        format: Int,
        width: Int,
        height: Int
    ) {
    }

    fun update(delta: Long) {
        maids.forEach { it.update(delta, width, height) }
    }

    private fun createMaid(resourceId: Int, startX: Float, startY: Float): MovingMaid {
        val bitmap = BitmapFactory.decodeResource(resources, resourceId)
        val sprite = Sprite(bitmap, bitmap.width / 8, bitmap.height / 4)
        val player = Player(sprite).apply {
            x = startX
            y = startY
        }
        return MovingMaid(player)
    }

    fun render(canvas: Canvas) {
        canvas.drawBitmap(background, null, Rect(0, 0, canvas.width, canvas.height), paint)
        maids.forEach { it.draw(canvas) }
    }

    private class MovingMaid(private val player: Player) {
        private var moveX = 0f
        private var moveY = 0f
        private var directionChangeTimer = 0L

        init {
            chooseRandomDirection()
        }

        fun update(delta: Long, screenWidth: Int, screenHeight: Int) {
            directionChangeTimer -= delta
            if (directionChangeTimer <= 0L) {
                chooseRandomDirection()
                directionChangeTimer = Random.nextLong(700L, 1_800L)
            }

            player.move(moveX, moveY, delta)
            keepOnScreen(screenWidth, screenHeight)
        }

        fun draw(canvas: Canvas) {
            player.draw(canvas)
        }

        private fun chooseRandomDirection() {
            val angle = Random.nextDouble(0.0, 2.0 * Math.PI)
            moveX = cos(angle).toFloat()
            moveY = sin(angle).toFloat()
        }

        private fun keepOnScreen(screenWidth: Int, screenHeight: Int) {
            val maxX = (screenWidth - player.width).coerceAtLeast(0).toFloat()
            val maxY = (screenHeight - player.height).coerceAtLeast(0).toFloat()

            if (player.x <= 0f || player.x >= maxX) moveX = -moveX
            if (player.y <= 0f || player.y >= maxY) moveY = -moveY

            player.x = player.x.coerceIn(0f, maxX)
            player.y = player.y.coerceIn(0f, maxY)
        }
    }

}