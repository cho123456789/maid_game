package com.example.made_game

import android.graphics.Bitmap
import android.graphics.Rect

class Sprite(
    private val sheet: Bitmap,
    private val frameWidth: Int,
    private val frameHeight: Int
) {

    fun getFrame(column: Int, row: Int): Rect {

        return Rect(
            column * frameWidth,
            row * frameHeight,
            (column + 1) * frameWidth,
            (row + 1) * frameHeight
        )
    }

    fun bitmap(): Bitmap = sheet

    fun frameWidth() = frameWidth

    fun frameHeight() = frameHeight
}