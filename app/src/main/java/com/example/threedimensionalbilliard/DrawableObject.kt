package com.example.threedimensionalbilliard

import android.graphics.Canvas

interface DrawableObject {
    val color: Int
    //val distance: Float
    fun draw(canvas: Canvas, camera: Camera)
    fun getDistance(point: Vector) : Float
}