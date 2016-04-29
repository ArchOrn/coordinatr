package com.smartnsoft.coordinatr.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * @author Raphael Kiffer
 * @since 2016.04.29
 */
public class CustomBehavior
    extends CoordinatorLayout.Behavior<ImageView>
{

  public CustomBehavior()
  {
  }

  public CustomBehavior(Context context, AttributeSet attrs)
  {
    super(context, attrs);
  }

  @Override
  public boolean layoutDependsOn(CoordinatorLayout parent, ImageView child, View dependency)
  {
    return dependency instanceof NestedScrollView;
  }

  @Override
  public boolean onDependentViewChanged(CoordinatorLayout parent, ImageView child, View dependency)
  {
    final CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
    layoutParams.height = dependency.getTop();
    layoutParams.width = layoutParams.height;
    child.setLayoutParams(layoutParams);

    child.setY(dependency.getY());

    return true;
  }
}
