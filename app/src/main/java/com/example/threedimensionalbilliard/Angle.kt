package com.example.threedimensionalbilliard

import android.util.Log

class Angle(private var ctg: Float, private var isPositive: Boolean) {// val
    companion object {
        fun fromCos(cosx: Float, isP: Boolean) : Angle {
            var cosDenom = cosx
            if (cosx > 1f) {
                cosDenom = 1 - Vector.epsilon//.. сщы
            }
            if (cosx < -1f) {
                cosDenom = -1 + Vector.epsilon
            }
            return Angle(cosx / Vector.sqrt(1 - cosDenom * cosDenom), isP)
        }
        fun fromTan(tgx: Float, isP: Boolean) : Angle {
            var tgDenom = tgx
            if (tgx == 0f) {
                tgDenom += Vector.epsilon
            }
            //Log.d("deb", "cyg is ${1 / tgDenom}")//000
            return Angle(1 / tgDenom, isP)
        }
    }
    fun toCos() : Float {
        //Log.d("deb", "cos den s ${1 + ctg * ctg}")
        //Log.d("deb", "cos denom is ${Vector.sqrt(1 + ctg * ctg)}")
        return ctg / Vector.sqrt(1 + ctg * ctg)
    }
    fun toSin() : Float {
        //Log.d("deb", "Sin den s ${1 + ctg * ctg}")
        //Log.d("deb", "sin denom is ${Vector.sqrt(1 + ctg * ctg)}")
        if (isPositive) {
            return 1 / Vector.sqrt(1 + ctg * ctg)
        } else {
            return -1 * 1 / Vector.sqrt(1 + ctg * ctg)
        }
    }
    fun toTan() : Float {
        var tgDenom = ctg
        if (tgDenom == 0f) {
            tgDenom = Vector.epsilon
        }
        if (isPositive) {
            return 1 / tgDenom
        } else {
            return -1 * 1 / tgDenom
        }
//////        return
    }
    fun toCtg() : Float {
        if (isPositive) {
            return ctg
        } else {
            return -1 * ctg
        }
    }
}