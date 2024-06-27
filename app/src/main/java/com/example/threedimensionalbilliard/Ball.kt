package com.example.threedimensionalbilliard

import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import kotlinx.coroutines.newFixedThreadPoolContext

class Ball(var position: Vector, private var velocity: Vector, private val radius: Float, val k: Float, override val color: Int) : DrawableObject {//radius  radias  float
    override fun draw(canvas: Canvas, camera: Camera) {

       val paint = Paint()
        paint.apply {
            style = Paint.Style.FILL_AND_STROKE
            setStrokeWidth(2f)
            color = this@Ball.color
            setAntiAlias(true)
        }

       Vector.V.run {
           val pp = Vector(camera.position, position)

            if (normS(pp) > radius * radius && dotP(pp, camera.dV) > 0) {

                canvas.drawCircle(camera.width / 2 + dotP(camera.j, pp) * camera.d / dotP(pp, camera.dV), camera.height / 2 - dotP(camera.i, pp) * camera.d / dotP(pp, camera.dV), camera.d * radius / sqrt(normS(pp) - radius * radius), paint) //x
            }
           if (normS(pp) <= radius * radius) {
               canvas.drawCircle(camera.width / 2f, camera.height / 2f, (sqrt((camera.width * camera.width + camera.height * camera.height).toFloat())) / 2, paint)
           }
        }


    }
    override fun getDistance(point: Vector) : Float {

        if (Vector.normS(Vector(point, position)) < radius * radius) {
            return 0f//retirn
        } else {
            return Vector.normS(Vector(point, position)) - 2 * Vector.sqrt(Vector.normS(Vector(point, position))) * radius + radius * radius//returtn
        }
    }
    fun move(dt: Float, obstacles: Array<Polygon>, dtInit: Float = dt) {
        Log.d("deb", "move")//
        val ts: Array<Float> = Array(obstacles.size, {i: Int -> 0f -1f})
        for (i in 0..(obstacles.size - 1)) {
            val obstacle = obstacles[i]
            Vector.run {
                val n = crossP(Vector(obstacle.a, obstacle.b), Vector(obstacle.a, obstacle.c))

                var denom = dotP(n, velocity)
                if (denom == 0f) {
                    denom = epsilon
                }
                if (denom * dotP(n, Vector(obstacle.a, position)) < 0f) {
                    val tOne = (-1 * radius * sqrt(normS(n)) - dotP(Vector(obstacle.a, position), n)) / denom
                    val tTwo = (radius * sqrt(normS(n)) - dotP(Vector(obstacle.a, position), n)) / denom
                    if (tOne >= 0f && tTwo >= 0f) {
                        if (tOne <= tTwo && tOne <= dt) {
                            ts[i] = tOne
                        }
                        if (tOne >= tTwo && tTwo <= dt) {
                            ts[i] = tTwo
                        }
                    }
                    if (tOne >= 0f && tTwo <= 0f && tOne <= dt) {
                        ts[i] = tOne
                    }
                    if (tTwo > 0f && tOne <= 0f  && tTwo <= dt) {
                        ts[i] = tTwo
                    }
                }

            }


        }
        var minAt = -1
        for (i in 0..(ts.size - 1)) {
            if (ts[i] > 0f && (minAt > 0 && ts[i] < ts[minAt] || minAt < 0)) {
                minAt = i;
            }
        }

        if (minAt >= 0) {
            //Log.d("deb", "non neg mion in at ${minAt}")//"deb"
            position.add(velocity.mulC(ts[minAt]))
            val n = Vector.crossP(Vector(obstacles[minAt].a, obstacles[minAt].b), Vector(obstacles[minAt].a, obstacles[minAt].c))
            velocity.add(n.mulC(-2 * Vector.dotP(velocity, n) / Vector.normS(n)))
            move(dt - ts[minAt], obstacles, dtInit)
        } else {
            position.add(velocity.mulC(dt))
            velocity.mul(k * dtInit)
            if (Vector.normS(velocity) < Vector.epsilon * Vector.epsilon) {
                velocity = Vector()
            }
        }
    }
    fun hit(f: Vector, a: Vector) {
        //Log.d("deb", "hit")
        val pc = Vector(position, f)
        pc.mul(-1f)
        //Log.d("deb", "val b is ${Vector.dotP(pc, a)} ")
        if (Vector.dotP(pc, a) > 0f && Vector.dotP(pc, a) * Vector.dotP(pc, a) > Vector.normS(a) * (Vector.normS(pc) - radius * radius) ) {
            //Log.d("deb", "hit 2 hit ii II")
            Vector.run {
                val start = a.mulC((dotP(pc, a) - sqrt(radius * radius * normS(a) - normS(pc) * normS(a) + dotP(pc, a) * dotP(pc, a))) / normS(a))
                val aff = Vector(start, position)    //..мфд
                aff.mul(0.03512f * dotP(aff, a) / sqrt(normS(a) * normS(aff)))
                velocity = aff

            }

        }
    }
}