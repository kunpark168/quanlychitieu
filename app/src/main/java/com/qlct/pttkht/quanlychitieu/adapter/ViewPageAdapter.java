package com.qlct.pttkht.quanlychitieu.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.qlct.pttkht.quanlychitieu.fragment.AccountFragment;
import com.qlct.pttkht.quanlychitieu.fragment.ChitieuFragment;
import com.qlct.pttkht.quanlychitieu.fragment.KeHoachFragment;
import com.qlct.pttkht.quanlychitieu.fragment.TaiChinhFragment;

public class ViewPageAdapter extends FragmentPagerAdapter {

    public ViewPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                return new TaiChinhFragment();
            case 1:
                return new ChitieuFragment();
            case 2:
                return new KeHoachFragment();
            case 3:
                fragment = new AccountFragment();
            default:
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
