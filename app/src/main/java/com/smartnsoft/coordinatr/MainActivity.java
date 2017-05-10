package com.smartnsoft.coordinatr;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.smartnsoft.coordinatr.fragment.DefaultFragment;
import com.smartnsoft.coordinatr.fragment.FlingBehaviorFragment;
import com.smartnsoft.coordinatr.fragment.ScrollBehaviorFragment;
import com.smartnsoft.coordinatr.fragment.SimpleBehaviorFragment;
import com.smartnsoft.coordinatr.fragment.ViewPagerFragment;
import coordinatr.smartnsoft.com.coordinatr.R;

public class MainActivity
    extends AppCompatActivity
{

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.main_activity);

    final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
    final NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
    navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
    {
      @Override
      public boolean onNavigationItemSelected(MenuItem menuItem)
      {
        if (menuItem.isChecked())
        {
          menuItem.setChecked(false);
        }
        else
        {
          menuItem.setChecked(true);
        }

        drawerLayout.closeDrawers();

        final Fragment fragment;
        switch (menuItem.getItemId())
        {
          default:
          case R.id.defaultCase:
            fragment = new DefaultFragment();
            break;
          case R.id.simpleCase:
            fragment = new SimpleBehaviorFragment();
            break;
          case R.id.scrollCase:
            fragment = new ScrollBehaviorFragment();
            break;
          case R.id.flingCase:
            fragment = new FlingBehaviorFragment();
            break;
          case R.id.viewPagerCase:
            fragment = new ViewPagerFragment();
            break;
        }

        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();

        return true;
      }
    });

    final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.fragmentContainer, new DefaultFragment());
    transaction.commit();
  }
}
