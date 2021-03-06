package com.example.ysh.catolicos.app;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ysh.catolicos.app.sync.CatolicosSyncAdapter;


public class MainActivity extends ActionBarActivity implements tab_paroquias.Callback{

    private Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    String Paroquias = new String();
    String Confissoes  = new String();
    String Missas  = new String();

    /*
        Indicara se eh tablet ou handset
            se false - handset
            se true  - tablet ou high size screen
    */
    boolean mTwoPane;

    /*
        Sync Adapter part
    */
    public static final String AUTHORITY = "com.example.android.datasync.provider";
    public static final String ACCOUNT_TYPE = "ysh.example.com";
    public static final String ACCOUNT = "dummyaccount";
    Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        Criando um Acount Dummy
        */
        mAccount = CreateSyncAccount(this);
        mTwoPane = false;

        CharSequence Titles[]   ={"Paróquias","Missas","Feed"};
        int Numboftabs          = 3;

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_main);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(
                new SlidingTabLayout.TabColorizer() {
                    @Override
                    public int getIndicatorColor(int position) {
                        return getResources().getColor(R.color.tabsScrollColor);
                    }
                });
        tabs.setViewPager(pager);

        //todo : [Gato] -- Acredito que esse não seja o melhor lugar para se chamar a inicialização do syncAdapter.
        CatolicosSyncAdapter.initializeSyncAdapter(this);

        Utilities mydate = new Utilities();
        mydate.myhour("14:00");
   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        }

        return newAccount;
    }



    /*
        Implementacao da interface para chamar a CallBack
     */
    @Override
    public void onItemSelected(String date) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putString(String.valueOf(R.string.Intent_detailview_paroquia), date);

            DetailFragmentParoquia fragment = new DetailFragmentParoquia();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.paroquia_detail_container, fragment)
                    .commit();
        } else {

            /*
                Sempre antes de chamar uma activity eh necessario lembrar de declara-la no manifest file
            */
            Intent intent = new Intent(this, DetailActivityParoquia.class)
                    .putExtra(String.valueOf(R.string.Intent_detailview_paroquia), date);

            startActivity(intent);
        }
    }

}
