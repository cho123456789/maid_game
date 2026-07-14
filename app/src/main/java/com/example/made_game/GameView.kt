package com.example.made_game

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class GameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {

    private lateinit var gameLoop: GameLoop
    private lateinit var player: Player

    // 랜덤 이동 방향과 다음 방향 변경까지의 시간(ms)
    private var moveX = 0f
    private var moveY = 0f
    private var directionChangeTimer = 0L

    init {
        holder.addCallback(this)
        isFocusable = true
    }

    override fun surfaceCreated(holder: SurfaceHolder) {

        val bitmap = BitmapFactory.decodeResource(
            resources,
            R.drawable.maid_test
        )

        // 리소스 밀도에 의해 비트맵 크기가 바뀌어도 8×4 시트의 한 칸 전체를 사용한다.
        val sprite = Sprite(
            bitmap,
            bitmap.width / 8,
            bitmap.height / 4
        )

        player = Player(sprite)

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

        directionChangeTimer -= delta

        if (directionChangeTimer <= 0L) {
            chooseRandomDirection()
            directionChangeTimer = Random.nextLong(700L, 1_800L)
        }

        player.move(moveX, moveY, delta)
        keepPlayerOnScreen()
    }

    private fun chooseRandomDirection() {
        val angle = Random.nextDouble(0.0, 2.0 * Math.PI)
        moveX = cos(angle).toFloat()
        moveY = sin(angle).toFloat()
    }

    private fun keepPlayerOnScreen() {
        val maxX = (width - player.width).coerceAtLeast(0).toFloat()
        val maxY = (height - player.height).coerceAtLeast(0).toFloat()

        if (player.x <= 0f || player.x >= maxX) {
            moveX = -moveX
        }
        if (player.y <= 0f || player.y >= maxY) {
            moveY = -moveY
        }

        player.x = player.x.coerceIn(0f, maxX)
        player.y = player.y.coerceIn(0f, maxY)
    }

    fun render(canvas: Canvas) {

        canvas.drawColor(Color.WHITE)

        player.draw(canvas)
    }

}