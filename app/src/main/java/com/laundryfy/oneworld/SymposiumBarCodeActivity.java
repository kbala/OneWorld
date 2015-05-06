package com.laundryfy.oneworld;


import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class SymposiumBarCodeActivity   extends ActionBarActivity {
    private TextView textView;
    private TextView footerTextView;
    private Button send;
    private String profileType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symposiumbarcode);

        textView = (TextView) findViewById(R.id.textView);
        footerTextView = (TextView) findViewById(R.id.symposiumFooterText);
        send = (Button) findViewById(R.id.badge_send);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gift, menu);
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


    public void send(View view)
    {
        Intent intent = new Intent(this, ConfirmActivity.class);
        intent.putExtra("parent", SymposiumBarCodeActivity.class.getName());
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 100)
        {
            try {
                String message = data.getStringExtra("MESSAGE");
                int code = Integer.parseInt(message);
                profileType="Y";
                String address = "John Doe\n123 4th Avenue\nNew York\nNY 10003";
                switch (code)
                {
                    case 1:
                        address = "Joe Scrocco\n450 North End Avenue\nNew York\nNY 10282";

                        break;
                    case 2:
                        address =  "Paul Tan\n19 St, Marks Place\nNew York\nNY 10003";

                        break;
                    case 3:
                        address = "John Smith\n805 Broadway\nNew York\nNY 10003";

                        break;
                    case 4:
                        address = "Alex Gitlin\n250 Bedford Avenue\nBrooklyn\nNY 11211";

                        break;
                    case 5:
                        address = "Mark Porzio\n1235 2nd Avenue \nNew York\nNY 10013";

                        break;
                }
                textView.setText(address);
                footerTextView.setVisibility(View.VISIBLE);
                send.setEnabled(true);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
