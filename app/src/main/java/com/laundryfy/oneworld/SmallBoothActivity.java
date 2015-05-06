package com.laundryfy.oneworld;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.text.Html;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnKeyListener;

import com.dynamsoft.tessocr.TessOCR;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SmallBoothActivity extends ActionBarActivity {

    private TessOCR mTessOCR;
    private ProgressDialog mProgressDialog;
    private String mCurrentPhotoPath;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_PICK_PHOTO = 2;

    private TextView textView;
    private TextView footerTextView;
    private EditText zipcodeEditView;
    public static final int RESULT_TEXT = 2;

    private Button send;
    private String resultUrl = "result.txt";
    private   RadioGroup radioGroupCustoms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_small_booth);

        mTessOCR = new TessOCR(this);

        AppController.InitializeProgressDialog(this, "Progressing");

        textView = (TextView) findViewById(R.id.textView);
        footerTextView = (TextView) findViewById(R.id.smallBoothFooterText);
        zipcodeEditView = (EditText) findViewById(R.id.zipcode);
        radioGroupCustoms = (RadioGroup) findViewById(R.id.smallBooth_Customs);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setDisplayShowHomeEnabled(true);
        send = (Button) findViewById(R.id.smallBooth_send);

        zipcodeEditView.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            if (zipcodeEditView.getText().toString().trim().length()>=5) {
                                int selectedId = radioGroupCustoms.getCheckedRadioButtonId();
                                RadioButton selectedCustom = (RadioButton) findViewById(selectedId);
                                SetOSP(selectedCustom.getTag().toString());
                            }
                        }
                        return false;
                    }
                });
        ((RadioButton)findViewById(R.id.radioButton1)).setText(Html.fromHtml("Viagra<sup><small>®</small></sup>"));
        ((RadioButton)findViewById(R.id.radioButton2)).setText(Html.fromHtml("Eliquis<sup><small>®</small></sup>"));
        radioGroupCustoms.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int selectedId) {
                        RadioButton selectedCustom = (RadioButton) findViewById(selectedId);
                        SetOSP(selectedCustom.getTag().toString());

                    }
                });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_small_booth, menu);
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
            textView.setText("");
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }




    public void send(final View view) {

        try {
            RadioGroup radioGroupCustoms = (RadioGroup) findViewById(R.id.smallBooth_Customs);
            int selectedId = radioGroupCustoms.getCheckedRadioButtonId();

            // find the radiobutton by returned id
            RadioButton selectedCustom = (RadioButton) findViewById(selectedId);

            if (null != selectedCustom) {
                AppController.ShowProgressDialog();
                ApiClient.post("product?productid="+selectedCustom.getTag().toString(), null, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            AppController.HideProgressDialog();
                            JSONObject jsonObject = response.getJSONObject("Data");
                            if (jsonObject.getBoolean("Success")) {
                                Intent intent = new Intent(SmallBoothActivity.this, ConfirmActivity.class);
                                intent.putExtra("parent", SmallBoothActivity.class.getName());
                                startActivity(intent);
                                finish();
                            }
                        } catch (Exception e) {
                            AppController.HideProgressDialog();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        AppController.HideProgressDialog();
                        Intent intent = new Intent(SmallBoothActivity.this, ConfirmActivity.class);
                        intent.putExtra("parent", SmallBoothActivity.class.getName());
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        AppController.HideProgressDialog();
                        Intent intent = new Intent(SmallBoothActivity.this, ConfirmActivity.class);
                        intent.putExtra("parent", SmallBoothActivity.class.getName());
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        AppController.HideProgressDialog();
                        if (statusCode == 0) {
                            Toast.makeText(SmallBoothActivity.this, "No Network Connection!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(SmallBoothActivity.this, responseString, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        AppController.HideProgressDialog();
                        if (statusCode == 0) {
                            Toast.makeText(SmallBoothActivity.this, "No Network Connection!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(SmallBoothActivity.this, errorResponse.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                Toast.makeText(SmallBoothActivity.this, "Please select a product", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            AppController.HideProgressDialog();
            e.printStackTrace();
        }

    }


    private void uriOCR(Uri uri) {
        if (uri != null) {
            InputStream is = null;
            try {
                is = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                doOCR(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        Intent intent = getIntent();
        if (Intent.ACTION_SEND.equals(intent.getAction())) {
            Uri uri = (Uri) intent
                    .getParcelableExtra(Intent.EXTRA_STREAM);
            uriOCR(uri);
        }
    }




    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    /**
     * http://developer.android.com/training/camera/photobasics.html
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        String storageDir =Environment.getExternalStorageDirectory()
                + "/TessOCR";
        File dir = new File(storageDir);
        if (!dir.exists())
            dir.mkdir();

        File image = new File(storageDir + "/" + imageFileName + ".jpg");

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == REQUEST_TAKE_PHOTO
                && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
            doOCR(bitmap);
        }
        else if (requestCode == REQUEST_PICK_PHOTO
                && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                uriOCR(uri);
            }
        }
    }


    private void pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_PHOTO);
    }

    public void takePhoto(View view) {
        dispatchTakePictureIntent();
    }

    private void doOCR(final Bitmap bitmap) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(this, "Processing",
                    "Processing Image...", true);
        }
        else {
            mProgressDialog.show();
        }

        new Thread(new Runnable() {
            public void run() {
                //Bitmap temp=applyOrientation(bitmap,  ExifInterface.ORIENTATION_ROTATE_270);
                final String result = mTessOCR.getOCRResult(bitmap);

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (result != null && !result.equals("")) {
                            textView.setText(result);
                        }
                        zipcodeEditView.setVisibility(View.VISIBLE);
                        send.setEnabled(true);
                        mProgressDialog.dismiss();
                    }

                });

            };
        }).start();
    }

    private void SetOSP(String selectedId){
        String OSP;

        if (zipcodeEditView.getText().toString().trim().length()>=5) {

            if  (selectedId.equals("1")){
                OSP="V(65/98) C(45/23) L(0/0) PP(Y) T";
            }else{
                OSP="E(34/78) X(56/21) P(87/21) PP(Y) T";
            }

            footerTextView.setText(OSP);
            footerTextView.setVisibility(View.VISIBLE);
        }
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
