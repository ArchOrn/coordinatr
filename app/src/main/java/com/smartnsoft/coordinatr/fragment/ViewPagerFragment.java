package com.smartnsoft.coordinatr.fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import coordinatr.smartnsoft.com.coordinatr.R;

/**
 * @author Raphael Kiffer
 * @since 2017.05.09
 */
public class ViewPagerFragment
    extends Fragment
{

  public static final class ViewPagerAdapter
      extends FragmentStatePagerAdapter
  {

    private final List<WeakReference<Fragment>> fragments;

    public ViewPagerAdapter(FragmentManager fragmentManager, List<WeakReference<Fragment>> fragments)
    {
      super(fragmentManager);

      this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position)
    {
      return fragments.get(position).get();
    }

    @Override
    public int getCount()
    {
      return fragments.size();
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    final View view = inflater.inflate(R.layout.view_pager_fragment, container, false);
    final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);

    final List<WeakReference<Fragment>> fragments = new ArrayList<>();
    fragments.add(new WeakReference<Fragment>(new DefaultFragment()));
    fragments.add(new WeakReference<Fragment>(new DefaultFragment()));

    viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), fragments));

    return view;
  }
}
