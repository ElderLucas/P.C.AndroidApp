package com.example.ysh.catolicos.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager

    /*
        Aqui nesse if tem um comportamento bem interessante:
        Ao escorregar as tbs entre 0, 1 e 2 observa-se que quando estou na primeira Tab, Zero, a Tab 1 vai ser criada tbm, prq sera necessario que a tab vizinha esteja criada
        Ou seja, qndo estou na 2, a 1 estara "criada" tambem.e qndo estou a 0 a um tambem.
      */

    @Override
    public Fragment getItem(int position) {

        if(position == 0) // if the position is 0 we are returning the First tab
        {
            tab_paroquias tab_paroquia = new tab_paroquias();
            return tab_paroquia;

        }
        else if (position == 1){
            tab_missas tab_missa = new tab_missas();
            return tab_missa;
        }
        else             // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            //tab_confissoes tab_conf = new tab_confissoes();
            VolleyController volley = new VolleyController();
            return volley;
        }


    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {

        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {

        return NumbOfTabs;
    }
}