package cn.zicoo.ir2teledemo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import cn.zicoo.ir2teledemo.model.Category;

import java.util.List;

/**
 * Created by wxy on 16/6/24.
 */
public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragments;
    private List<Category> categories;

    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<Category> categories) {
        super(fm);
        this.fragments = fragments;
        this.categories = categories;
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
        return categories.get(position).Name;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }
}
