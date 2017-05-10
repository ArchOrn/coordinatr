package com.smartnsoft.coordinatr.fragment;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
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
public class DefaultFragment
    extends Fragment
{

  private CollapsingToolbarLayout collapsingToolbarLayout;

  private Toolbar toolbar;

  private TextView textView;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    final View view = inflater.inflate(R.layout.default_fragment, container, false);

    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    final AppCompatActivity activity = (AppCompatActivity) getActivity();
    activity.setSupportActionBar(toolbar);

    collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsingToolbarLayout);
    collapsingToolbarLayout.setTitle(getString(R.string.defaultTitle));

    textView = (TextView) view.findViewById(R.id.textView);
    textView.setText(getString(R.string.assembleSpace, getString(R.string.lipsum), getString(R.string.lipsum), getString(R.string.lipsum), getString(R.string.lipsum)));

    return view;
  }
}
