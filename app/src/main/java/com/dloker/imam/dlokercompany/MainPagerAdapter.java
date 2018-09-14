package com.dloker.imam.dlokercompany;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

class MainPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public MainPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int i) {

        switch (i) {
            case 0:
                HomeFragment tab1 = new HomeFragment();
                return tab1;
            case 1:
                DiterimaFragment tab2 = new DiterimaFragment();
                return tab2;
            case 2:
                DitolakFragment tab3 = new DitolakFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }
}


