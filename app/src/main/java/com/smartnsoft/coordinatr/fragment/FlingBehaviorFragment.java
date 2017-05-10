package com.smartnsoft.coordinatr.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import coordinatr.smartnsoft.com.coordinatr.R;

/**
 * @author Raphael Kiffer
 * @since 2015.12.24
 */
public class FlingBehaviorFragment
    extends Fragment
{

  private Toolbar toolbar;

  private TextView textView;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    final View view = inflater.inflate(R.layout.fling_behavior_fragment, container, false);

    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbar.setTitle(R.string.flingTitle);
    final AppCompatActivity activity = (AppCompatActivity) getActivity();
    activity.setSupportActionBar(toolbar);

    textView = (TextView) view.findViewById(R.id.textView);
    textView.setText(getString(R.string.assembleSpace, getString(R.string.lipsum), getString(R.string.lipsum), getString(R.string.lipsum), getString(R.string.lipsum)));

    return view;
  }
}
