package com.smartnsoft.coordinatr.view;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;

import com.smartnsoft.coordinatr.behavior.ScrollBehavior;

/**
 * @author Raphael Kiffer
 * @since 2016.04.28
 */
@CoordinatorLayout.DefaultBehavior(ScrollBehavior.class)
public class ScrollLayout
    extends AppBarLayout
{

  public ScrollLayout(Context context)
  {
    super(context);
  }

  public ScrollLayout(Context context, AttributeSet attrs)
  {
    super(context, attrs);
  }
}
