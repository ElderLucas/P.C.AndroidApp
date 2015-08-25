package com.example.ysh.catolicos.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by YSH on 22/08/2015.
 */
public class InfoDetailParoquia extends ActionBarActivity implements FragmentManager.OnBackStackChangedListener {

    public static final String DATE_KEY = "paroquia_setected";
    private Toolbar Detailtoolbar;

    /*
        Intent relational data
    */
    Intent intent;
    String ParoquiaSelected = "Minha Paróquia";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infodetail);

        Detailtoolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(Detailtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*
            Recebe Intent
        */

        //intent = getIntent();
       // ParoquiaSelected = intent.getStringExtra(String.valueOf(R.string.Intent_detailview_paroquia));

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
            arguments.putString(String.valueOf(R.string.info_detail_paroquia), ParoquiaSelected);

            InfoDetailFragment fragment = new InfoDetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.infodetail_container, fragment)
                    .commit();
        }

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




}
