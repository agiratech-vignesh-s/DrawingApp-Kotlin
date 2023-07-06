package com.example.drawingapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import kotlin.coroutines.coroutineContext

class DrawingView(context: Context,attri:AttributeSet):View(context,attri ) {
    private var drawpath:Custompath?=null
    private var canvasBitmap:Bitmap?=null
    private var drawPaint: Paint?=null
    private var canvasPaint:Paint? =null
    private var color: Int =Color.BLACK
    private var brushSize:Float=0.toFloat()
    private var canvas:Canvas?=null
    private var path=ArrayList<Custompath>()


init {
  SerUpDrawing()
}

    fun SerUpDrawing(){
        drawPaint= Paint()
        drawpath=Custompath(color,brushSize)
        drawPaint!!.color=color
        drawPaint!!.style=Paint.Style.STROKE
        drawPaint!!.strokeCap=Paint.Cap.ROUND
        drawPaint!!.strokeJoin=Paint.Join.ROUND
        canvasPaint= Paint(Paint.DITHER_FLAG)
//        brushSize=20.toFloat()
    }

    fun setBrushSize(newSize:Float){
  brushSize=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,newSize,resources.displayMetrics)
        drawPaint!!.strokeWidth=brushSize
    }

    internal inner class Custompath(var color:Int,var brushThickness:Float): Path() {

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(canvasBitmap!!,0f,0f,canvasPaint)
        for (p in path){
            drawPaint!!.strokeWidth=p.brushThickness
            drawPaint!!.color=p.color
            canvas.drawPath(p,drawPaint!!)
        }

   drawPaint!!.strokeWidth=drawpath!!.brushThickness
        drawPaint!!.color=drawPaint!!.color
        canvas.drawPath(drawpath!!,drawPaint!!)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        canvasBitmap=Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888)
        canvas= Canvas(canvasBitmap!!)
    }

    fun setColor(newColor:String){
        color=Color.parseColor(newColor)
        drawpath!!.color=color
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchx=event?.x
        val touchy=event?.y
        when(event?.action){
            MotionEvent.ACTION_DOWN->{
                drawpath!!.color=color
                drawpath!!.brushThickness=brushSize
                drawpath!!.reset()
                drawpath?.moveTo(
                    touchx!!,
                    touchy!!
                ) // Set t

            }
            MotionEvent.ACTION_MOVE->{
                if (touchx != null && touchy !=null)
                drawpath!!.lineTo(touchx,touchy)
            }

            MotionEvent.ACTION_UP->{
                path.add(drawpath!!)

                drawpath=Custompath(color,brushSize)

            }
            else->return false

        }
        invalidate()
        return true

    }
}