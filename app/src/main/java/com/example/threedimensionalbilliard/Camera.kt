package com.example.threedimensionalbilliard

import android.graphics.Canvas
import android.util.Log
import kotlin.properties.Delegates

class Camera(var position: Vector, val width: Int, val height: Int) {
    var i = Vector(0f, 1f, 0f)//:
    var j = Vector(0f, 0f, 1f)
    var dV = Vector(1f, 0f, 0f)
    var d by Delegates.notNull<Float>()
    constructor(position: Vector, width: Int, height: Int, phiCos: Float) : this(position, width, height) {//var
        d = Vector.sqrt((width * width + height * height) * (1 + phiCos) / (1 - phiCos)) / 2
        //this.width =
    }
    fun draw(canvas: Canvas, scene: MutableList<DrawableObject>) {
        scene.sortWith({objOne, objTwo -> -1 * objOne.getDistance(position).compareTo(objTwo.getDistance(position))})
        for (obj in scene) {
            obj.draw(canvas, this)
        }
    }
    fun move(delta: Vector) {
        position.add(delta)
    }
    fun getDeltas(dx: Float, dy: Float) {
        //deb
        //Log.d("deb", "rot 1")
        var deltaAbs = 0f
        if (dx * dx > Vector.epsilon * Vector.epsilon && dx * dx > dy * dy) {
            deltaAbs = dx
            if (deltaAbs < 0f) {
                deltaAbs *= -1f
            }
            //Log.d("deb", "rot 2 a")
            i.mul(-1f)
            rotate(i, Angle.fromTan(deltaAbs / d, dx >= 0), j)
            rotate(i, Angle.fromTan(deltaAbs / d, dx >= 0), dV)
            i.mul(-1f)
            //Log.d("deb", "rot 3 a")//111
        }
        if (dy * dy > Vector.epsilon * Vector.epsilon && dy * dy > dx * dx) {
            deltaAbs = dy
            if (deltaAbs < 0f) {
                deltaAbs *= -1f
            }
            //Log.d("deb", "tot 2 b")
            rotate(j, Angle.fromTan(deltaAbs / d, dy >= 0), i)
            rotate(j, Angle.fromTan(deltaAbs / d, dy >= 0), dV)
            //Log.d("deb", "rot 3 b")
        }
        //Log.d("deb", "rot 4")
    }
    fun rotate(axis: Vector, angle: Angle, vec: Vector) {

        val prevPos = Vector(vec.x, vec.y, vec.z)
        vec.x =      prevPos.x          * angle.toCos() - prevPos.y * axis.z * angle.toSin() + prevPos.z * axis.y * angle.toSin()
        vec.y =      prevPos.x * axis.z * angle.toSin() + prevPos.y          * angle.toCos() - prevPos.z * axis.x * angle.toSin()
        vec.z = -1 * prevPos.x * axis.y * angle.toSin() + prevPos.y * axis.x * angle.toSin() + prevPos.z          * angle.toCos()

    }

}