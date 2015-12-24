package com.smartnsoft.coordinatr.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import coordinatr.smartnsoft.com.coordinatr.R;

/**
 * @author Raphael Kiffer
 * @since 2015.12.24
 */
public class MainFragment
    extends Fragment
{

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    final View view = inflater.inflate(R.layout.main_fragment, container, false);

    return view;
  }
}
