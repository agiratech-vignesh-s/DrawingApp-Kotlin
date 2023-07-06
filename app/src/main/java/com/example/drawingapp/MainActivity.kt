package com.example.drawingapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get

class MainActivity : AppCompatActivity() {
    private var drawingView:DrawingView?=null
    private var currentPiantBotton:ImageButton?=null
    var requestPermission: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                val perMissionName = it.key
                val isGranted = it.value
                if (isGranted ) {
                    Toast.makeText(
                        this,
                        "Permission granted now you can read the storage files.",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    if (perMissionName == android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        Toast.makeText(
                            this,
                            "Oops you just denied the permission.",
                            Toast.LENGTH_LONG
                        ).show()
                }
            }

        }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var brush=findViewById<ImageButton>(R.id.paintBrush!!)
        drawingView=findViewById(R.id.drawing_view)
        drawingView?.setBrushSize(20.toFloat())
        val linearLayoutPaintColor=findViewById<LinearLayout?>(R.id.ll_paint_color)
        val gallery:ImageButton=findViewById(R.id.selectImage)
        val undo:ImageButton=findViewById(R.id.undo)
        currentPiantBotton=linearLayoutPaintColor[1] as ImageButton
        brush.setOnClickListener(){
            showBrushSizeChooserDialogBox()
        }
        undo.setOnClickListener(){
                
        }
        gallery.setOnClickListener(){
            requestStoragePermission()
        }
        currentPiantBotton!!.setImageDrawable(
            ContextCompat.getDrawable(this,R.drawable.pallet_pressed)
        )
    }



    @SuppressLint("SuspiciousIndentation")
    private fun showBrushSizeChooserDialogBox(){
     val brushDialog=Dialog(this)
        brushDialog.setContentView(R.layout.dialoge_brush_size)
        brushDialog.setTitle("Brush Size")
        val smallbtn=brushDialog.findViewById<ImageButton>(R.id.small)
        smallbtn.setOnClickListener(){
            drawingView?.setBrushSize(10.toFloat())
            brushDialog.dismiss()
        }
        val midumbtn=brushDialog.findViewById<ImageButton>(R.id.midum)
        midumbtn.setOnClickListener(){
            drawingView?.setBrushSize(20.toFloat())
            brushDialog.dismiss()
        }
        val largebtn=brushDialog.findViewById<ImageButton>(R.id.large)
        largebtn.setOnClickListener(){
            drawingView?.setBrushSize(30.toFloat())
            brushDialog.dismiss()
        }
        brushDialog.show()
    }

    fun paintClicked(view: View) {
        if (view !==currentPiantBotton){
            val imageButton=view as ImageButton
            val colorTag=imageButton.tag.toString()
            drawingView!!.setColor(colorTag)
            imageButton.setImageDrawable(
                ContextCompat.getDrawable(this,R.drawable.pallet_pressed)
            )
            currentPiantBotton!!.setImageDrawable(
                ContextCompat.getDrawable(this,R.drawable.pallet_normal)
            )
            currentPiantBotton=view
        }
    }

    private fun requestStoragePermission(){
        if (
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
        ){
            showRationaleDialog("Kids Drawing App","Kids Drawing App " +
                    "needs to Access Your External Storage")
        }
        else {
            requestPermission.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                   Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        }

    }
    private fun showRationaleDialog(
        title: String,
        message: String,
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }
}