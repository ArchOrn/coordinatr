package com.smartnsoft.coordinatr.behavior;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * @author Raphael Kiffer
 * @since 2016.04.28
 */
public class HeaderHalfBarBehavior
    extends AppBarLayout.Behavior
{

  private static final int IMAGE_MINIMUM_HEIGHT = 200;

  private Toolbar toolbar;

  private ImageView imageView;

  private int maxHeight;

  public HeaderHalfBarBehavior()
  {
  }

  public HeaderHalfBarBehavior(Context context, AttributeSet attrs)
  {
    super(context, attrs);
  }

  @Override
  public boolean onLayoutChild(CoordinatorLayout parent, AppBarLayout child, int layoutDirection)
  {
    if (imageView == null)
    {
      for (int index = 0; index < child.getChildCount(); index++)
      {
        final View childAt = child.getChildAt(index);
        if (childAt instanceof ImageView)
        {
          imageView = (ImageView) childAt;
          imageView.setMinimumHeight(HeaderHalfBarBehavior.IMAGE_MINIMUM_HEIGHT);
        }

        if (childAt instanceof Toolbar)
        {
          toolbar = (Toolbar) childAt;
        }
      }
    }

    return false;
  }

  @Override
  public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child, View directTargetChild, View target,
      int nestedScrollAxes)
  {
    return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
  }

  @Override
  public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy,
      int[] consumed)
  {
  }

  @Override
  public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dxConsumed,
      int dyConsumed, int dxUnconsumed, int dyUnconsumed)
  {
    super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);

    final CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();

    if (layoutParams.height < 0 && maxHeight == 0)
    {
      maxHeight = child.getHeight();
      layoutParams.height = child.getHeight();
    }

    if (dyConsumed > 0 && layoutParams.height > HeaderHalfBarBehavior.IMAGE_MINIMUM_HEIGHT + toolbar.getHeight())
    {
      if (layoutParams.height - dyConsumed >= HeaderHalfBarBehavior.IMAGE_MINIMUM_HEIGHT + toolbar.getHeight())
      {
        layoutParams.height -= dyConsumed;
      }
      else
      {
        layoutParams.height = HeaderHalfBarBehavior.IMAGE_MINIMUM_HEIGHT + toolbar.getHeight();
      }
    }

    // TODO: Add dyConsumed < 0
    // TODO: Add color change
    // TODO: Add fling

    child.setLayoutParams(layoutParams);
  }
}
