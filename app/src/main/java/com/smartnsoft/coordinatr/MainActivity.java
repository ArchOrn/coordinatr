package com.smartnsoft.coordinatr;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.smartnsoft.coordinatr.fragment.DefaultFragment;
import com.smartnsoft.coordinatr.fragment.ScrollBehaviorFragment;
import coordinatr.smartnsoft.com.coordinatr.R;

public class MainActivity
    extends AppCompatActivity
{

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_activity);

    final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.fragmentContainer, new DefaultFragment());
    transaction.commit();
  }
}
