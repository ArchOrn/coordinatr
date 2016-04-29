package com.smartnsoft.coordinatr.behavior;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Raphael Kiffer
 * @since 2016.04.28
 */
public class HeaderHalfBarBehavior
    extends AppBarLayout.Behavior
{

  public HeaderHalfBarBehavior()
  {
  }

  public HeaderHalfBarBehavior(Context context, AttributeSet attrs)
  {
    super(context, attrs);
  }

  @Override
  public boolean layoutDependsOn(CoordinatorLayout parent, AppBarLayout child, View dependency)
  {
    return super.layoutDependsOn(parent, child, dependency);
  }

  @Override
  public boolean onDependentViewChanged(CoordinatorLayout parent, AppBarLayout child, View dependency)
  {
    return super.onDependentViewChanged(parent, child, dependency);
  }
}
