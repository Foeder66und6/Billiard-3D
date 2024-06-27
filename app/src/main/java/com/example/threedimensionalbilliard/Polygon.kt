package com.example.threedimensionalbilliard

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.Log

class Polygon(val a: Vector, val b: Vector, val c: Vector, override val color: Int) : DrawableObject {//: Long  private
    override fun draw(canvas: Canvas, camera: Camera) {

        //Log.d("deb", "draw call")
        val front = mutableListOf<Vector>()
        val behind = mutableListOf<Vector>()
        if (Vector.dotP(Vector(camera.position, a), camera.dV) > 0) {//,
            front.add(a)
        } else {
            behind.add(a)
        }
        if (Vector.dotP(Vector(camera.position, b), camera.dV) > 0) {
            front.add(b)
        } else {
            behind.add(b)
        }
        if (Vector.dotP(Vector(camera.position, c), camera.dV) > 0) {
            front.add(c)
        } else {
            behind.add(c)
        }

        val verticices = mutableListOf<Vector>()
        Vector.V.run {
            for (i in 0..(front.size - 1)) {
                val pc = Vector(camera.position, front[i])
                var denom = dotP(camera.dV, pc)
                if (denom == 0f ) {
                    denom = epsilon
                }
                front[i] = Vector(dotP(camera.j, pc), dotP(camera.i, pc), 0f).mulC(camera.d / denom)
            }
            for (i in 0..(behind.size - 1)) {
                val pc = Vector(camera.position, behind[i])
                var denom =  dotP(camera.dV, pc)
                if (denom == 0f) {
                    denom = -1 * epsilon
                }
                behind[i] = Vector(dotP(camera.j, pc), dotP(camera.i, pc), 0f).mulC(camera.d / denom)
            }

            var ca: Vector = Vector(1f, 0f, 0f)
            var cb: Vector = Vector(0f, 1f, 0f)
            var ab: Vector = Vector(0f, 0f, 1f)
            var cc: Vector = Vector(1f, 1f, 1f)
            var t: Float
            if (front.size == 1) {
                ca = Vector(behind[0], front[0])
                cb = Vector(behind[1], front[0])
                ab = Vector(behind[0], behind[1])
                cc = front[0]
            }

            if (front.size == 2) {
                ca = Vector(behind[0], front[0])
                cb = Vector(behind[0], front[1])
                ab = Vector(front[0], front[1])
                cc = behind[0]

            }

            var denom =  normS(crossP(cb, ca))
            if (denom == 0f) {
                denom += epsilon
            }
//            Log.d("deb", "den is ${denom}")
//            Log.d("deb", "another val is ${(normS(cc)  + camera.width * camera.width + camera.height * camera.height) * normS(ab)}")//Log.dd
            t =  (normS(cc)  + camera.width * camera.width + camera.height * camera.height) * normS(ab) / denom  / 1000f + 250f


            if (front.size == 3) {
                t = 1f
            }
//            Log.d("deb", "t calc")

            if (front.size != 3 && front.size != 0) {
                verticices.add(front[0].addC(ca.mulC(t)))
            }
            for (ver in front) {
                verticices.add(ver)
            }
            if (front.size != 3 && front.size != 0) {
                verticices.add(front[front.size - 1].addC(cb.mulC(t)))
            }
            Log.d("deb", "arr Built")//


        }
        if (front.size != 0) {
            val paint = Paint().apply {//Paint//
                style = Paint.Style.FILL_AND_STROKE
                strokeWidth = 2f//
                isAntiAlias = true//
                setColor(this@Polygon.color)
            }//
            val shape = Path()//
            shape.moveTo(camera.width / 2 + verticices[0].x, camera.height / 2 - verticices[0].y)//
            for (i in 1..(verticices.size)) {
                shape.lineTo((camera.width / 2f + verticices[i % verticices.size].x), camera.height / 2f - verticices[i % verticices.size].y)
            }//
            shape.close()//
            canvas.drawPath(shape, paint)//

        }


        val aaaa = MutableList<Vector>(0, {Vector(0f, 0f, 0f)})
        aaaa.add(a)

    }


    override fun getDistance(point: Vector) : Float {//vector:
//        ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//        Log.d("deb", "dist call")
        val n = Vector.crossP(a.addC(c.mulC(-1f)), b.addC(c.mulC(-1f)))
        val projection = Vector.sum(n.mulC(Vector.dotP(c.addC(point.mulC(-1f)), n) / Vector.normS(n)), point)
        if (-1 * Vector.epsilon < triSur(a, b, c) - triSur(a, b, projection) - triSur(a, projection, c) - triSur(projection, b, c) && triSur(a, b, c) - triSur(a, projection, c) - triSur(projection, b, c) - triSur(a, b, projection) < Vector.epsilon) {
            return Vector.dotP(n, Vector(a, point)) * Vector.dotP(n, Vector(a, point)) / Vector.normS(n)
        }

        val arr = arrayOf(distTo(a, b, point), distTo(b, c, point), distTo(c, a, point))
        var minAt = -1
        for (i in 0..2) {    // var
            if (arr[i] >= 0f && (minAt < 0 || minAt >= 0 && arr[i] < arr[minAt])) {
                minAt = i
            }
        }
        if (minAt >= 0) {
            return arr[minAt]
        }

        arr[0] = Vector.normS(Vector(a, point))
        arr[1] = Vector.normS(Vector(b, point))//0
        arr[2] = Vector.normS(Vector(c, point))
        minAt = 0
        for (i in 0..2) {
            if (arr[i] < arr[minAt]) {
                minAt = i
            }
        }
        return arr[minAt]


    }
    private fun triSur(aa: Vector, bb: Vector, cc: Vector) : Float {
        return Vector.sqrt(Vector.normS(Vector.crossP(Vector(bb, aa), Vector(cc, aa)))) / 2
    }
    private fun distTo(aa: Vector, bb: Vector, p: Vector) : Float {
        val ab = Vector(aa, bb)
        val ap = Vector(aa, p)
        val t = Vector.dotP(Vector(aa, p), ab) / Vector.dotP(ab, ab)

        if (t < 0 || t > 1) {
            return -1f
        } else {
            return (Vector.normS(ab) * Vector.normS(ap) - Vector.dotP(ap, ab) * Vector.dotP(ap, ab)) / Vector.normS(ab)// vector
        }
    }

    private fun getProj(point: Vector, n: Vector, mZero: Vector   ) : Vector {

        return Vector.run {

            sum(n.mulC(dotP(Vector(point, mZero), n) / normS(n)), point)
        }
    }
}