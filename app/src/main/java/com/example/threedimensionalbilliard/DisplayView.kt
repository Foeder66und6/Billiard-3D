package com.example.threedimensionalbilliard

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Toast
import kotlin.concurrent.thread

class  DisplayView : SurfaceView, SurfaceHolder.Callback {

    lateinit var cnt: Context
    lateinit var surfaceHolderO: SurfaceHolder
    lateinit var forwardButton: RoundButton
    lateinit var backwardButton: RoundButton
    private val vel = 0.00625f * 3f
    private lateinit var camera: Camera
    private val l = 20f;
    val aaf = Vector(l, -1 * l / 2, -1 * l / 2)
    val bbf = Vector(l, -1 * l / 2, l / 2)
    val ccf = Vector(l, l / 2, l / 2)
    val ddf = Vector(l, l / 2, -1 * l / 2)
    val aab = Vector(-1 * l, -1 * l / 2, -1 * l / 2)
    val bbb = Vector(-1 * l, -1 * l / 2, l / 2)
    val ccb = Vector(-1 * l, l / 2, l / 2)
    val ddb = Vector(-1 * l, l / 2, -1 * l / 2)
    val colorUp = (2 * Color.GREEN + Color.WHITE) / 3
    val colorDown = (2 * Color.GREEN + Color.BLACK) / 3
    val colorForward = (2 * Color.GREEN + Color.BLUE) / 3
    val colorBackward = (2 * Color.GREEN + Color.RED) / 3
    private val scene: Array<Polygon> = arrayOf(
        Polygon(ddf, aaf, bbf, colorForward), Polygon(bbf, ccf, ddf, colorForward),//Polu
        Polygon(aab, aaf, ddf, Color.GREEN), Polygon(aab, ddb, ddf, Color.GREEN),
        Polygon(ddb, ddf, ccf, colorUp), Polygon(ddb, ccb, ccf, colorUp),
        Polygon(ccb, ccf, bbf, Color.GREEN), Polygon(ccb, bbb, bbf, Color.GREEN),
        Polygon(bbb, bbf, aaf, colorDown), Polygon(bbb, aab, aaf, colorDown),
        Polygon(ddb, aab, bbb, colorBackward), Polygon(bbb, ccb, ddb, colorBackward)

    )
    private val balls: Array<Ball> = arrayOf(Ball(Vector(1f, 0f, 0f), Vector(), 1f, 0.0375f, getResources().getColor(R.color.white)))//pprivare v , 0.75f / 20f , 0.0375f , 0.00375f , 0.000375f , 0.0000375f , 0.000375f , 0.5f Vector(0.5f, 0.5f, 0.5f)



    //
    //
    //
    //


    //.
    //        .
    //
    //
    //
    //
    private var cx: Float = 0f
    private var cy: Float = 0f
    private var cw: Int = 0
    private var ch: Int = 0
    private var tx: Float? = null
    private var ty: Float? = null
    private    var dx: Float = 0f;

    private var dy: Float = 0f;
    private var scaleDelta: Float = 0.1075f * 1.5f * 2f * 2f * 2f * 1.5f * 1.25f
    private var isUp: Boolean = true;
    private var isFirstDrawing = true
    var delay = 20
    var isRunnung = true//end
    var r = 0;



    constructor(context: Context) : super(context) {
        surfaceHolderO = getHolder()
        //setFocusable(true)
        surfaceHolderO.addCallback(this)
        cnt = context
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
       // r += delay
       // r = r % 200//0  + 100
       // val paint: Paint = Paint()

        if (isFirstDrawing) {    //..ша
            init()
            isFirstDrawing = false// flase
        }

//        r += 100
//        paint.style = Paint.Style.FILL_AND_STROKE
//        paint.color = getResources().getColor(R.color.white)
//        canvas.drawCircle(cw - r.toFloat(), ch - r.toFloat(), r.toFloat(), paint)
//        r -= 100



        forwardButton.isPressed(tx, ty)
        backwardButton.isPressed(tx, ty)
        if (tx != null && ty != null) {
            camera.getDeltas(dx * scaleDelta, -1 * dy * scaleDelta)
            //Log.d("deb", "rot s beg")
        }

        val sc: MutableList<DrawableObject> = mutableListOf()

        for (poly in scene) {
            sc.add(poly)
        }
        for (ball in balls) {
            sc.add(ball)
        }
        camera.draw(canvas, sc)
        for (ball in balls) {
            ball.move(delay.toFloat(), scene)
            //drawTextVector(canvas, 20f, 110f, Vector(camera.position, ball.position))
            //drawTextVector(canvas, 20f, 132f, camera.dV.mulC(camera.d).addC(camera.j.mulC(cx - cw / 2)).addC(camera.i.mulC(-1 * cy + ch / 2)).mulC(1f / Vector.sqrt(((cx - ch / 2) * (cx - ch / 2) + (-1 * cy + ch / 2) * (-1 * cy + ch / 2)))))
            //drawTextVector(canvas, 20f, 154f, camera.dV.mulC(camera.d).addC(camera.j.mulC(cx - cw / 2).addC(camera.i.mulC(ch / 2 - cy)).mulC(1f / Vector.sqrt(((cx - cw / 2) * (cx - cw / 2) + (ch / 2 - cy) * (ch / 2 - cy))) / 5f)))//////////
        }
        forwardButton.draw(canvas)
        backwardButton.draw(canvas)
        //drawTextVector(canvas, 20f, 22f, camera.position)//drav
        //drawTextVector(canvas, 20f, 66f, camera.j)
        //drawTextVector(canvas, 20f, 88f, camera.i)
        //drawTextVector(canvas, 20f, 44f, camera.dV)

        //drawTextVector(canvas, 20f, 110f, Vector(camera.position, (scene[scene.size - 1] as Ball).position))
    }

    override fun onTouchEvent(event: MotionEvent) : Boolean {
        //event.action == MotionEvent.CLICK
        if (event.action == MotionEvent.ACTION_DOWN) {
            cx = event.x
            cy = event.y
        }
        if (event.action != MotionEvent.ACTION_UP) {
            if (isUp) {
                tx = event.x
                ty = event.y
                isUp = false
            }
            val trtx = tx
            val trty = ty
            dx = event.x - tx!!
            dy = event.y - ty!!
            tx = event.x
            ty = event.y
        } else {
            if ((cx - event.x) * (cx - event.x) + (cy - event.y) * (cy - event.y) < Vector.epsilon * Vector.epsilon) {
                //Log.d("deb", "click!")
                for (ball in balls) {
                    ball.hit(camera.position, camera.dV.mulC(camera.d).addC(camera.j.mulC(cx - cw / 2).addC(camera.i.mulC(ch / 2 - cy)).mulC(1f / Vector.sqrt(((cx - cw / 2) * (cx - cw / 2) + (ch / 2 - cy) * (ch / 2 - cy))) / 5f)))
//                    drawTextVector()
                }
            }
            tx = null;
            ty = null;
            isUp = true
        }
        return true

    }

    override fun equals(other: Any?): Boolean {
         return true
    }

    fun init() {
        cw = getWidth()
        ch = getHeight()
        camera = Camera(Vector(-1f, 0f, 0f), cw, ch, -0.5f)
        val r = (cw + ch) / 18f
        val paint: Paint = Paint().apply {
            style = Paint.Style.STROKE
            setStrokeWidth(2f)
            color = getResources().getColor(R.color.white)
        }
        forwardButton = RoundButton(r, ((5 * ch - cw) / 6f), r, paint, {camera.move(camera.dV.mulC(delay * vel))})
        backwardButton = RoundButton(r, ((17 * ch - cw) / 18f), r, paint, {camera.move(camera.dV.mulC(-1 * delay * vel))})
        //Toast.makeText(cnt, "Initialization s finished", Toast.LENGTH_LONG)
    }

    fun drawTextVector(canvas: Canvas, x: Float, y: Float, vector: Vector) {
        val p = Paint()
        p.apply{
            color = getResources().getColor(R.color.white)//p.
        }
        canvas.drawText("${vector.x} + ${vector.y} + ${vector.z}, ${Vector.normS(vector)}", x, y - 2, p)
    }

    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {

        thread(start = true) {
            while (isRunnung) {
                val canvas: Canvas? = surfaceHolderO.lockCanvas()
                synchronized(surfaceHolderO) {
                    if (canvas != null) {
                        draw(canvas)

                    }

                }
                if (canvas != null) {
                    surfaceHolderO.unlockCanvasAndPost(canvas)
                }
                Thread.sleep(delay.toLong())

            }
            //Thread.
        }
    }
    override fun surfaceChanged(surfaceHolder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }
    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        var retry = true
        isRunnung = false
        while (retry) {
            try {

            } catch (e: InterruptedException) {

            }
        }
        //Thread.join()
    }

}