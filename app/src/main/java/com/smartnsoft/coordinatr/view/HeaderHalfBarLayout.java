package com.smartnsoft.coordinatr.view;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;

import com.smartnsoft.coordinatr.behavior.HeaderHalfBarBehavior;

/**
 * @author Raphael Kiffer
 * @since 2016.04.28
 */
@CoordinatorLayout.DefaultBehavior(HeaderHalfBarBehavior.class)
public class HeaderHalfBarLayout
    extends AppBarLayout
{

  public HeaderHalfBarLayout(Context context)
  {
    super(context);
  }

  public HeaderHalfBarLayout(Context context, AttributeSet attrs)
  {
    super(context, attrs);
  }
}
