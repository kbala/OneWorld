package com.laundryfy.oneworld;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;


public class GiftActivity extends ActionBarActivity {

    private TextView textView;
    private TextView footerTextView;
    private Button send;

    private CheckBox food1;
    private CheckBox food2;
    private CheckBox food3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift);

        textView = (TextView) findViewById(R.id.textView);

        food1 = (CheckBox)findViewById(R.id.food1);
        food2 = (CheckBox)findViewById(R.id.food2);
        food3 = (CheckBox)findViewById(R.id.food3);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setDisplayShowHomeEnabled(true);
        send = (Button)findViewById(R.id.gift_send);
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
        if(food1.isChecked() || food2.isChecked() || food3.isChecked()) {
            Intent intent = new Intent(this, ConfirmActivity.class);
            intent.putExtra("parent", GiftActivity.class.getName());
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(GiftActivity.this, "Please select atleast one gift", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 100)
        {
            try {
                String message = data.getStringExtra("MESSAGE");
                int code = Integer.parseInt(message);

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

                send.setEnabled(true);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
