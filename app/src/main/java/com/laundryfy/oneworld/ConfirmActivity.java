package com.laundryfy.oneworld;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class ConfirmActivity extends ActionBarActivity {

    private  Class parentClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setDisplayShowHomeEnabled(true);
        Handler handler = new Handler();
        try {
            String parent = getIntent().getStringExtra("parent");
            parentClass = Class.forName(parent);
            handler.postDelayed(new Runnable() {
                public void run() {

                    Intent intent = new Intent(ConfirmActivity.this, parentClass);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        }
        catch (ClassNotFoundException  e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_confirm, menu);
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

//    @Override
//    public void onRestart(){
//        super.onRestart();
//        Intent previewMessage = new Intent(ConfirmActivity.this, ConfirmActivity.class);
//        ActionBarActivity parentActivity = (ActionBarActivity)getParent();
//        parentActivity.startActivityFromChild(ConfirmActivity.this,previewMessage,1);
//        this.finish();
//    }
}
