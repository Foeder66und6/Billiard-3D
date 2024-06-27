package com.example.threedimensionalbilliard

class Vector(var x: Float, var y: Float, var z: Float) {//val

    constructor (start: Vector, end: Vector) : this(end.x - start.x, end.y - start.y, end.z - start.z)
    constructor() : this(0f, 0f, 0f)
    companion object V {
        val epsilon = 0.01f
        fun dotP(v_one: Vector, v_two: Vector) : Float {
            return v_one.x * v_two.x + v_one.y * v_two.y + v_one.z * v_two.z
        }
        fun crossP(v_one: Vector, v_two: Vector) : Vector {
            return Vector(v_one.y * v_two.z - v_one.z * v_two.y, -1 * (v_one.x * v_two.z - v_one.z * v_two.x), v_one.x * v_two.y - v_one.y * v_two.x)
        }
        fun normS(v: Vector) : Float {
            return Vector.dotP(v, v)
        }
        fun sum(v_one: Vector, v_two: Vector) : Vector {
            return Vector(v_one.x + v_two.x, v_one.y + v_two.y, v_one.z + v_two.z)
        }
        fun sqrt(a: Float, epsilon: Float = 0.001f) : Float {
            var low = 0f
            var high = a / 6000f + 1500f
            while (high - low > epsilon) {
                var middle = (low + high) / 2//:
                if ( middle == a / middle) {
                    break
                }
                if ( middle > a / middle) {
                    high = middle//l
                } else {
                    low = middle
                }
            }
            return (high + low) / 2
        }

    }
    fun add(v: Vector) {
        this.x += v.x
        this.y += v.y
        this.z += v.z
    }
    fun mul(k: Float) {//,
        this.x = this.x * k
        this.y = this.y * k
        this.z = this.z * k
    }
    fun mulC(k: Float) : Vector {
        return Vector(this.x * k, this.y * k, this.z * k)
    }
    fun addC(v: Vector) : Vector {
        return Vector(this.x + v.x, this.y + v.y, this.z + v.z)
    }
}