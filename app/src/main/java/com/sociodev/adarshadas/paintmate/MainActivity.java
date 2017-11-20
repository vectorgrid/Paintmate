package com.sociodev.adarshadas.paintmate;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,ActivityCompat.OnRequestPermissionsResultCallback{

    private DrawingCanvas drawingCanvas;
    private ImageButton currPaint, eraseBtn, newBtn, saveBtn;
    private float verySmallBrush, smallBrush, mediumBrush, largeBrush;
    private ImageButton brush, share;
    private boolean isShareClicked = false;


    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        drawingCanvas = (DrawingCanvas)findViewById(R.id.drawing);
        GridLayout paintLayout = (GridLayout)findViewById(R.id.paint_colors);
        currPaint = (ImageButton)paintLayout.getChildAt(7);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.color_selector));
        verySmallBrush = 5;
        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);
        brush = (ImageButton) findViewById(R.id.brush_btn);
        share = (ImageButton)findViewById(R.id.share_btn);
        eraseBtn = (ImageButton)findViewById(R.id.eraser_btn);
        newBtn = (ImageButton)findViewById(R.id.new_drawing_btn);
        saveBtn = (ImageButton)findViewById(R.id.save_btn);
        share.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        newBtn.setOnClickListener(this);
        eraseBtn.setOnClickListener(this);
        brush.setOnClickListener(this);
        drawingCanvas.setBrushSize(mediumBrush);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            //resume tasks needing this permission
            drawingCanvas.setDrawingCacheEnabled(true);
            SaveToGallery save = new SaveToGallery();
            if(isShareClicked){
                save.SaveImage(getApplicationContext(),drawingCanvas.getDrawingCache(),true, drawingCanvas);
                isShareClicked = false;
            }
            else{
                save.SaveImage(getApplicationContext(),drawingCanvas.getDrawingCache(),false, drawingCanvas);
            }
            drawingCanvas.destroyDrawingCache();

        }
    }

    public void paintClicked(View view){
        //use chosen color
        drawingCanvas.setErase(false);
        drawingCanvas.setBrushSize(drawingCanvas.getLastBrushSize());
        if(view!=currPaint){
            //update color
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.color_selector));
            currPaint.setImageResource(0);
            currPaint=(ImageButton)view;
            drawingCanvas.setColor(color);
        }
    }

    @Override
    public void onClick(View view){
        //respond to clicks
        if(view.getId()==R.id.brush_btn){
            //draw button clicked
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Brush size:");
            brushDialog.setContentView(R.layout.brush_selector);
            ImageButton verySmallBtn = (ImageButton)brushDialog.findViewById(R.id.verysmall_brush);
            verySmallBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawingCanvas.setBrushSize(verySmallBrush);
                    drawingCanvas.setLastBrushSize(verySmallBrush);
                    drawingCanvas.setErase(false);
                    brushDialog.dismiss();
                }
            });
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawingCanvas.setBrushSize(smallBrush);
                    drawingCanvas.setLastBrushSize(smallBrush);
                    drawingCanvas.setErase(false);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawingCanvas.setBrushSize(mediumBrush);
                    drawingCanvas.setLastBrushSize(mediumBrush);
                    drawingCanvas.setErase(false);
                    brushDialog.dismiss();
                }
            });

            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawingCanvas.setBrushSize(largeBrush);
                    drawingCanvas.setLastBrushSize(largeBrush);
                    drawingCanvas.setErase(false);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();
        }
        else if(view.getId()==R.id.eraser_btn){
            //switch to erase - choose size
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Eraser size:");
            brushDialog.setContentView(R.layout.brush_selector);

            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawingCanvas.setErase(true);
                    drawingCanvas.setBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawingCanvas.setErase(true);
                    drawingCanvas.setBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawingCanvas.setErase(true);
                    drawingCanvas.setBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();
        }
        else if(view.getId()==R.id.new_drawing_btn){
            //new button
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("New drawing");
            newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    drawingCanvas.startNew();
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            newDialog.show();
        }
        else if(view.getId()==R.id.save_btn){
            //save drawing
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("Save drawing to device Gallery?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    //save drawing
                    if(isStoragePermissionGranted()){
                        drawingCanvas.setDrawingCacheEnabled(true);
                        SaveToGallery save = new SaveToGallery();
                        save.SaveImage(getApplicationContext(),drawingCanvas.getDrawingCache(),false, drawingCanvas);
                        drawingCanvas.destroyDrawingCache();
                    }


                }
            });
            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            saveDialog.show();
        }
        else if(view.getId() == R.id.share_btn){
            isShareClicked = true;
            if(isStoragePermissionGranted()){
                drawingCanvas.setDrawingCacheEnabled(true);
                SaveToGallery save = new SaveToGallery();
                save.SaveImage(getApplicationContext(),drawingCanvas.getDrawingCache(),true, drawingCanvas);
                drawingCanvas.destroyDrawingCache();
                isShareClicked = false;
            }
        }
    }
}

