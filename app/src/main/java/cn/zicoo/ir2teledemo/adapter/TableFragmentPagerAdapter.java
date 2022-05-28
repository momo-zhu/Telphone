package cn.zicoo.ir2teledemo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

import cn.zicoo.ir2teledemo.model.Category;
import cn.zicoo.ir2teledemo.model.Place;

/**
 * Created by wxy on 16/6/24.
 */
public class TableFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragments;
    private List<Place> places;

    public TableFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<Place> places) {
        super(fm);
        this.fragments = fragments;
        this.places = places;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return places.get(position).PlaceTable;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }
}
