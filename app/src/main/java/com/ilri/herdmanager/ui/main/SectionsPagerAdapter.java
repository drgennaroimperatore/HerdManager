package com.ilri.herdmanager.ui.main;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ilri.herdmanager.R;
import com.ilri.herdmanager.classes.DynamicEventContainer;
import com.ilri.herdmanager.classes.HealthEventContainer;
import com.ilri.herdmanager.classes.ProductivityEventContainer;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;
    private int mHerdID = -155;
    AddHeardHealthEventFragment mHealthFragment = null;
    AddHerdProductivityFragment mProductivityFragment = null;
    AddHerdDynamicFragment mDynamicFragment =null;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    public SectionsPagerAdapter(Context context, FragmentManager fm, int herdID, Bundle readOnlyArguments) {
        super(fm);
        mContext = context;
        mHerdID = herdID;
        mHealthFragment = new AddHeardHealthEventFragment(mHerdID);
        mDynamicFragment = new AddHerdDynamicFragment();
        mProductivityFragment = new AddHerdProductivityFragment();

        if(readOnlyArguments!=null)
        {
            mHealthFragment.setArguments(readOnlyArguments);
            mDynamicFragment.setArguments(readOnlyArguments);
            mProductivityFragment.setArguments(readOnlyArguments);
        }
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if(position==0)
            return mHealthFragment;
        if(position==1)
            return mProductivityFragment;
        if(position==2)
            return mDynamicFragment;

        return PlaceholderFragment.newInstance(position + 1);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return TAB_TITLES.length;
    }

    public HealthEventContainer getHealthEventForVisit()
    {
        return mHealthFragment.getHealthEventContainer();

    }

    public DynamicEventContainer getDynamicEventForVisit()
    {
        return mDynamicFragment.getDynamicEventForHealthVisit();
    }


    public ProductivityEventContainer getProductivityEventForVisit() {

        return mProductivityFragment.getProductivityEventForHelthVisit();
    }
}