package com.laundryfy.oneworld;

import android.support.v7.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setDisplayShowHomeEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
          //  return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    public void launchBadgeScan(View view)
    {
        Intent intent = new Intent(this, BadgeScanActivity.class);
        startActivity(intent);

    }

    public void launchGiftForDoctor(View view)
    {
        Intent intent = new Intent(this, GiftActivity.class);
        startActivity(intent);

    }

    public void launchSmallBooth(View view)
    {
        Intent intent = new Intent(this, SmallBoothActivity.class);
        startActivity(intent);

    }

    public void launchSymposium(View view)
    {
        Intent intent = new Intent(this, SymposiumActivity.class);
        startActivity(intent);

    }

    public void launchSymposiumBarcode(View view)
    {
        Intent intent = new Intent(this, SymposiumBarCodeActivity.class);
        startActivity(intent);

    }
}
