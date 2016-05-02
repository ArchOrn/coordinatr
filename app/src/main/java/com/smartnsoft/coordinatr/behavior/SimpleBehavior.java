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
public class SimpleBehavior
    extends CoordinatorLayout.Behavior<ImageView>
{

  public SimpleBehavior()
  {
  }

  public SimpleBehavior(Context context, AttributeSet attrs)
  {
    super(context, attrs);
  }

  @Override
  public boolean layoutDependsOn(CoordinatorLayout parent, ImageView child, View dependency)
  {
    // Target the NestedScrollView child of the CoordinatorLayout as the dependency
    return dependency instanceof NestedScrollView;
  }

  @Override
  public boolean onDependentViewChanged(CoordinatorLayout parent, ImageView child, View dependency)
  {
    // Set the image height and width to the dependency top bound value (it's like the header image height)
    final CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
    layoutParams.height = dependency.getTop();
    layoutParams.width = layoutParams.height;
    child.setLayoutParams(layoutParams);

    // Set the image y position to the dependency top bound value
    child.setY(dependency.getTop());

    return true;
  }
}
