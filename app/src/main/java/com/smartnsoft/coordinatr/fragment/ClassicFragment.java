package com.smartnsoft.coordinatr.fragment;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import coordinatr.smartnsoft.com.coordinatr.R;

/**
 * @author Raphael Kiffer
 * @since 2015.12.24
 */
public class ClassicFragment
    extends Fragment
{

  private CollapsingToolbarLayout collapsingToolbarLayout;

  private Toolbar toolbar;

  private TextView textView;

  private FloatingActionButton floatingActionButton;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    final View view = inflater.inflate(R.layout.classic_fragment, container, false);

    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    final AppCompatActivity activity = (AppCompatActivity) getActivity();
    activity.setSupportActionBar(toolbar);

    collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsingToolbarLayout);
    collapsingToolbarLayout.setTitle(getString(R.string.coordinatorDefault));

    textView = (TextView) view.findViewById(R.id.textView);
    textView.setText(getString(R.string.assembleSpace, getString(R.string.lipsum), getString(R.string.lipsum), getString(R.string.lipsum), getString(R.string.lipsum)));

    floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floatingButton);
    floatingActionButton.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View view)
      {
        final FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, new SimpleBehaviorFragment());
        transaction.commit();
      }
    });

    return view;
  }
}
