package com.laundryfy.oneworld;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class SymposiumActivity extends ActionBarActivity {

    private ImageView imageView;
    private Button send_vermont;
    private Button send_minnesota;
    private Button send_massachusetts;
    private Button send_all_others;

    private final int TAKE_PICTURE = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symposium);
        imageView = (ImageView) findViewById(R.id.symposiumImageView);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setDisplayShowHomeEnabled(true);
        send_vermont = (Button) findViewById(R.id.send_vermont);
        send_minnesota = (Button) findViewById(R.id.send_minnesota);
        send_massachusetts = (Button) findViewById(R.id.send_massachusetts);
        send_all_others = (Button) findViewById(R.id.send_all_others);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_symposium, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_back) {
            System.gc();
            imageView.setImageBitmap(null);
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private static Uri getOutputMediaFileUri(){
        return Uri.fromFile(getOutputMediaFile());
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "ABBYY Cloud OCR SDK Demo App");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }

        // Create a media file name
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "image.jpg" );

        return mediaFile;
    }

    public void captureImageFromCamera( View view) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        Uri fileUri = getOutputMediaFileUri(); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        startActivityForResult(intent, TAKE_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)
            return;


        switch (requestCode) {
            case TAKE_PICTURE: {
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(getOutputMediaFile()));
//                    imageView.setImageBitmap(null);
//                    imageView.setImageURI(getOutputMediaFileUri());
                    int fileOrientation = resolveBitmapOrientation(getOutputMediaFile());
                    bitmap = applyOrientation(bitmap, fileOrientation);
                    imageView.setImageBitmap(null);
                    imageView.setImageBitmap(bitmap);
                    send_vermont.setEnabled(true);
                    send_all_others.setEnabled(true);
                    send_massachusetts.setEnabled(true);
                    send_minnesota.setEnabled(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
                break;
        }
    }

    public void send(final View view) {

        Intent intent = new Intent(this, ConfirmActivity.class);
        intent.putExtra("parent", SymposiumActivity.class.getName());
        startActivity(intent);
        finish();
    }

    public void sendConfirm(final View view) {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        Intent intent = new Intent(SymposiumActivity.this, ConfirmActivity.class);
                        intent.putExtra("parent", SymposiumActivity.class.getName());
                        startActivity(intent);
                        finish();
                        break;


                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(SymposiumActivity.this );
        builder.setMessage("Healthcare Professionals licensed in your state are prohibited from receiving this item.\n\n Thank you for your cooperation.").setPositiveButton("Yes", dialogClickListener);
        AlertDialog alert = builder.create();
        alert.show();
    }

    private static int resolveBitmapOrientation(File bitmapFile) throws IOException {
        ExifInterface exif = null;
        exif = new ExifInterface(bitmapFile.getAbsolutePath());

        return exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
    }

    private static Bitmap applyOrientation(Bitmap bitmap, int orientation) {
        int rotate = 0;
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotate = 270;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotate = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotate = 90;
                break;
            default:
                return bitmap;
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix mtx = new Matrix();
        mtx.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }



}
