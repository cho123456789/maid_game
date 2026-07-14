package com.example.made_game

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

class Player(
    private val sprite: Sprite
) {

    companion object {
        const val DOWN = 0
        const val LEFT = 1
        const val RIGHT = 2
        const val UP = 3
    }

    // 화면 위치
    var x = 300f
    var y = 300f

    // 이동 속도(px/s)
    var speed = 180f

    // 현재 방향
    var direction = DOWN

    val width: Int
        get() = sprite.frameWidth()

    val height: Int
        get() = sprite.frameHeight()

    // 걷기 애니메이션 (8프레임)
    private val animation = SpriteAnimation(
        frameCount = 8,
        frameDuration = 100
    )

    private var moving = false

    fun move(dx: Float, dy: Float, delta: Long) {

        moving = dx != 0f || dy != 0f

        if (!moving) {
            animation.reset()
            return
        }

        val distance = speed * delta / 1000f

        x += dx * distance
        y += dy * distance

        animation.update(delta)

        direction = when {
            kotlin.math.abs(dx) > kotlin.math.abs(dy) -> {
                if (dx > 0) RIGHT else LEFT
            }

            else -> {
                if (dy > 0) DOWN else UP
            }
        }
    }

    fun draw(canvas: Canvas) {

        val frame = animation.frame()

        val src = sprite.getFrame(
            frame,
            direction
        )

        val dst = Rect(
            x.toInt(),
            y.toInt(),
            x.toInt() + sprite.frameWidth(),
            y.toInt() + sprite.frameHeight()
        )

        canvas.drawBitmap(
            sprite.bitmap(),
            src,
            dst,
            Paint()
        )
    }
}