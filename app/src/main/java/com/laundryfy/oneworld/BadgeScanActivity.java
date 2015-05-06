package com.laundryfy.oneworld;

import android.content.Intent;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.provider.SyncStateContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.text.Html;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Set;


public class BadgeScanActivity extends ActionBarActivity {


    private TextView textView;
    private TextView footerTextView;
    private EditText emailAddressEditView;
    private Button send;
    private String profileType;
    private RadioGroup radioGroupCustoms;
    private  String in;
    private Boolean IsScanned;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badge_scan);
        profileType="Y";
        IsScanned=false;
        radioGroupCustoms = (RadioGroup) findViewById(R.id.badge_Customs);
        textView = (TextView) findViewById(R.id.textView);
        footerTextView = (TextView) findViewById(R.id.badgeFooterText);
        emailAddressEditView = (EditText) findViewById(R.id.email_address);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setDisplayShowHomeEnabled(true);
        send = (Button) findViewById(R.id.badge_send);
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
        setAddressFromSelect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_badge_scan, menu);
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
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void launchScanner(View view)
    {
        Intent intent = new Intent(this, ScanBarcodeActivity.class);
        startActivityForResult(intent, 100);
    }


    public void send(View view) {
        try {

            int selectedId = radioGroupCustoms.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                RadioButton selectedCustom = (RadioButton) findViewById(selectedId);

            if (null != selectedCustom) {
                ApiClient.post("product?productid="+selectedCustom.getTag().toString(), null, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject("Data");
                            if (jsonObject.getBoolean("Success")) {
                                Intent intent = new Intent(BadgeScanActivity.this, ConfirmActivity.class);
                                intent.putExtra("parent", BadgeScanActivity.class.getName());
                                startActivity(intent);
                                finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        Intent intent = new Intent(BadgeScanActivity.this, ConfirmActivity.class);
                        intent.putExtra("parent", BadgeScanActivity.class.getName());
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Intent intent = new Intent(BadgeScanActivity.this, ConfirmActivity.class);
                        intent.putExtra("parent", BadgeScanActivity.class.getName());
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        if (statusCode == 0) {
                            Toast.makeText(BadgeScanActivity.this, "No Network Connection!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(BadgeScanActivity.this, responseString, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        if (statusCode == 0) {
                            Toast.makeText(BadgeScanActivity.this, "No Network Connection!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(BadgeScanActivity.this, errorResponse.toString(), Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
            else {
                Toast.makeText(BadgeScanActivity.this, "Please select a product", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 100)
        {
            try {
                String message = data.getStringExtra("MESSAGE");
                int code = Integer.parseInt(message);
                if (code==3){
                    Intent intent = new Intent(this, SelectAddressActivity.class);
                    startActivity(intent);
                }
                String email="johndoe@doctor.com";
                String address = "John Doe\n123 4th Avenue\nNew York\nNY 10003";
                profileType="Y";
                switch (code)
                {
                    case 1:
                        address = "Joe Scrocco\n450 North End Avenue\nNew York\nNY 10282";
                        email="jscrocco@oneworldinc.com";
                        profileType="Y";
                        break;
                    case 2:
                        address =  "Paul Tan\n19 St, Marks Place\nNew York\nNY 10003";
                        email="paultan1080@gmail.com";
                        profileType="N";
                        break;
                    case 3:
                        address = "John Smith\n805 Broadway\nNew York\nNY 10003";
                        email="johnsmith@gmail.com";
                        profileType="Y";
                        break;
                    case 4:
                        address = "Alex Gitlin\n250 Bedford Avenue\nBrooklyn\nNY 11211";
                        email="alexg@gmail.com";
                        profileType="N";
                        break;
                    case 5:
                        address = "Mark Porzio\n1235 2nd Avenue \nNew York\nNY 10013";
                        email="markporzio@gmail.com";
                        profileType="Y";
                        break;
                }
                textView.setText(address);
                emailAddressEditView.setText(email);
                emailAddressEditView.setVisibility(View.VISIBLE);
                send.setEnabled(true);
                RadioButton selectedCustom = (RadioButton) findViewById(radioGroupCustoms.getCheckedRadioButtonId());
                SetOSP(selectedCustom.getTag().toString());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void SetOSP(String selectedId){
        String OSP;

         if (emailAddressEditView.getText().length()>0){
            RadioButton rdoRegister = (RadioButton)findViewById((R.id.rdoRegister));
            if  (selectedId.equals("1")){
                OSP="V(65/98) C(45/23) L(0/0) PP(" + profileType + ") T";
            }else{
                OSP="E(34/78) X(56/21) P(87/21) PP(" + profileType + ") T";
            }
            if (profileType.equals("N")){
                rdoRegister.setVisibility(View.VISIBLE);
            }
            footerTextView.setText(OSP);
            footerTextView.setVisibility(View.VISIBLE);
       }
    }
    public void setAddressFromSelect(){
        try {
            String parent = getIntent().getStringExtra("address");

            String email="";
            String address = "";
            profileType="";
            if (parent.equals("1") || parent.equals("2")){
                email="johnsmith@gmail.com";
                profileType="Y";
                if (parent.equals("1")){
                    address = "John Smith\n805 Broadway\nNew York\nNY 10003";
                }else{
                    address = "John Smith\n303 Broadway\n New York\nNY 10005";

                }
                textView.setText(address);

                emailAddressEditView.setText(email);
                emailAddressEditView.setVisibility(View.VISIBLE);
                send.setEnabled(true);
                RadioButton selectedCustom = (RadioButton) findViewById(radioGroupCustoms.getCheckedRadioButtonId());
                SetOSP(selectedCustom.getTag().toString());
            }
        }
        catch (Exception  e)
        {
            e.printStackTrace();
        }

    }
}
