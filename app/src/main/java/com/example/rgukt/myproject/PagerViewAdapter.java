package com.example.rgukt.myproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class PagerViewAdapter  extends FragmentPagerAdapter{
    public PagerViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                ProfileFragment profileFragment=new ProfileFragment();
                return profileFragment;
            case 1:
                UsersFragment usersFragment=new UsersFragment();
                return usersFragment;
            default:
                return null;
        }

     }

    @Override
    public int getCount() {
        return 2;
    }
}
