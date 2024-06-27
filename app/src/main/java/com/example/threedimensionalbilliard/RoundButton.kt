package com.example.threedimensionalbilliard

import android.graphics.Canvas
import android.graphics.Paint

class RoundButton(val cx: Float, val cy: Float, val radius: Float, val paint: Paint, val action: () -> Unit) {
    fun isPressed(tx: Float?, ty: Float?) {
        if (tx != null && ty != null && ((tx - cx) * (tx - cx) + (ty - cy) * (ty - cy) <= radius * radius)) {//000
            action()
        }
    }
    fun draw(canvas: Canvas) {//, Paint

        canvas.drawCircle(cx, cy, radius, paint)
    }

}