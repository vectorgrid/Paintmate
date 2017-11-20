package com.sociodev.adarshadas.paintmate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Adarsha Das on 08-11-2017.
 */

public class SaveToGallery {
    private Context TheThis;
    private String nameoffolder = "/Paintmate";
    private String nameoffile;
    private File dir;
    public  File getDir(){
        return dir;
    }

    public String getNameoffile(){
        return nameoffile;
    }

    public  void SaveImage(Context context, Bitmap imagetosave, boolean isShareClicked, DrawingCanvas drawingCanvas){
        if(drawingCanvas.getCanvasDirtyState()){
            save(context,imagetosave,drawingCanvas);
        }
        if(isShareClicked){
            if(drawingCanvas.getCanvasDirtyState()){
                save(context,imagetosave,drawingCanvas);
            }
            DrawingCanvas.LastSavedData lastSavedData = drawingCanvas.getLastSavedData();
            share(context, lastSavedData.getLastSavedDir(),lastSavedData.getLastSavedNameOfFile());
        }
    }

    private void save(Context context, Bitmap imagetosave, DrawingCanvas drawingCanvas){
        TheThis = context;
        String filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + nameoffolder;
        SimpleDateFormat df3 = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss-a");
        String nameoffile = df3.format(new Date())+".jpg";
        File dir = new File(filepath);

        if(!dir.exists()){
            dir.mkdirs();
        }

        File file = new File(dir,nameoffile);
        try{
            FileOutputStream fout = new FileOutputStream(file);
            imagetosave.compress(Bitmap.CompressFormat.JPEG,100,fout);
            fout.flush();
            fout.close();
            FileCreatedAndAvailable(file);
            AbleToSave();
            DrawingCanvas.LastSavedData lastSavedData = drawingCanvas.getLastSavedData();
            lastSavedData.setLastSavedDir(dir);
            lastSavedData.setLastSavedNameOfFile(nameoffile);
            drawingCanvas.setCanvasDirtyState(false);
        }
        catch (FileNotFoundException e){}
        catch (IOException e){}
    }
    private void share(Context context, File dir, String nameoffile){
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpg");
        final File photoFile = new File(dir, nameoffile);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(photoFile));
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Painted from my PaintIt checkout: https://stackoverflow.com/questions/5214764/how-to-share-photo-with-caption-via-android-share-intent-on-facebook");
        context.startActivity(Intent.createChooser(shareIntent, "Share image using"));
    }
    private void AbleToSave() {
        Toast savedToast = Toast.makeText(TheThis,
                "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
        savedToast.show();
    }

    private void FileCreatedAndAvailable(File file) {
        MediaScannerConnection.scanFile(TheThis,
                new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener(){
                    public void onScanCompleted(String path, Uri uri){
                        Log.e("ExternalStorage", "Scanned" + path + ":");
                        Log.e("ExternalStorage","->Uri = "+uri);
                    }
                });
    }
}
