package com.example.made_game

class SpriteAnimation(

    private val frameCount: Int,
    private val frameDuration: Long

) {

    private var currentFrame = 0

    private var timer = 0L

    fun update(delta: Long) {

        timer += delta

        if (timer >= frameDuration) {

            timer = 0

            currentFrame++

            if (currentFrame >= frameCount)
                currentFrame = 0
        }
    }

    fun frame(): Int {

        return currentFrame
    }

    fun reset() {

        currentFrame = 0
        timer = 0
    }
}