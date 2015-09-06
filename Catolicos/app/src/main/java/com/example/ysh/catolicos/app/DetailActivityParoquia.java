package com.example.ysh.catolicos.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by YSH on 10/08/2015.
 */
public class DetailActivityParoquia extends ActionBarActivity implements FragmentManager.OnBackStackChangedListener, DetailFragmentParoquia_ListViewAdapter.Callback {

    public static final String DATE_KEY = "paroquia_setected";
    private Toolbar Detailtoolbar;

    /*
        Intent relational data
    */
    Intent intent;
    String ParoquiaSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_paroquia);

        Detailtoolbar = (Toolbar) findViewById(R.id.tool_bar);
        Detailtoolbar.setTitleTextColor(getResources().getColor(R.color.colorToolBarText));
        setSupportActionBar(Detailtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        /*
            Recebe Intent
        */

        intent = getIntent();
        ParoquiaSelected = intent.getStringExtra(String.valueOf(R.string.Intent_detailview_paroquia));

        /*
            Rename toolbar
        */
        setSupportActionBar(Detailtoolbar);
        getSupportActionBar().setTitle(ParoquiaSelected);


        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            //String date = getIntent().getStringExtra(DATE_KEY);

            Bundle arguments = new Bundle();
            arguments.putString(String.valueOf(R.string.Intent_detailview_paroquia), ParoquiaSelected);

            DetailFragmentParoquia fragment = new DetailFragmentParoquia();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.paroquia_detail_container, fragment)
                    .commit();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putString(String.valueOf(R.string.Intent_detailview_paroquia),ParoquiaSelected);
        //savedInstanceState.putInt(STATE_LEVEL, mCurrentLevel);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        //mCurrentScore = savedInstanceState.getInt(STATE_SCORE);
        //mCurrentLevel = savedInstanceState.getInt(STATE_LEVEL);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Bundle my = getIntent().getExtras();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int mId = android.R.id.home;

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    public void shouldDisplayHomeUp(){
        //Enable Up button only  if there are entries in the back stack
        boolean canback = getSupportFragmentManager().getBackStackEntryCount()>0;
    }


    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        getSupportFragmentManager().popBackStack();
        return true;
    }


    /*
        Metodo da INTERFACE com Fragmento DetailFragment
    */
    @Override
    public void showMyParishOnMap(Uri local_paroquiaURI) {
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, local_paroquiaURI);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }


    /*
        Metodo da INTERFACE com Fragmento DetailFragment
    */
    @Override
    public void showMyParishInfo(String[] idParoquia) {
        Intent intent = new Intent(this, InfoDetailParoquia.class).putExtra(String.valueOf(R.string.info_detail_paroquia), idParoquia);
        startActivity(intent);
    }
}
