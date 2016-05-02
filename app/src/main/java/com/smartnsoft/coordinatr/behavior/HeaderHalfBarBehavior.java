package com.smartnsoft.coordinatr.behavior;

import java.util.concurrent.atomic.AtomicBoolean;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.CoordinatorLayout.LayoutParams;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.widget.ImageView;

/**
 * @author Raphael Kiffer
 * @since 2016.04.28
 */
public class HeaderHalfBarBehavior
    extends AppBarLayout.Behavior
    implements AnimationListener
{

  private static class ToggleHeightColorAnimation
      extends Animation
  {

    private final View view;

    private final int minHeight;

    private final int maxHeight;

    private final boolean expand;

    public ToggleHeightColorAnimation(View view, int maxHeight, boolean expand, int duration,
        AnimationListener animationListener)
    {
      this(view, 0, maxHeight, expand, duration, animationListener);
    }

    public ToggleHeightColorAnimation(View view, int minHeight, int maxHeight, boolean expand, int duration,
        AnimationListener animationListener)
    {
      this.view = view;
      this.minHeight = minHeight;
      this.maxHeight = maxHeight;
      this.expand = expand;

      this.setDuration(duration);
      this.setAnimationListener(animationListener);
      this.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation transformation)
    {
      final ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
      layoutParams.height = (int) (expand == true ? (interpolatedTime == 1 ? ViewGroup.LayoutParams.WRAP_CONTENT : minHeight + maxHeight * interpolatedTime) : maxHeight - ((maxHeight - minHeight) * interpolatedTime));
      view.setLayoutParams(layoutParams);
    }


    @Override
    public boolean willChangeBounds()
    {
      return true;
    }

  }

  private static final int IMAGE_MINIMUM_HEIGHT = 200;

  private static final int ANIMATION_DURATION = 400;

  private static final int MINIMUM_SCROLL_DELTA = 10;

  private Toolbar toolbar;

  private ImageView imageView;

  private int maxHeight;

  private AtomicBoolean isAnimating;

  public HeaderHalfBarBehavior()
  {
    isAnimating = new AtomicBoolean(false);
  }

  public HeaderHalfBarBehavior(Context context, AttributeSet attrs)
  {
    super(context, attrs);

    isAnimating = new AtomicBoolean(false);
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

    if (maxHeight == 0)
    {
      maxHeight = child.getHeight();
      layoutParams.height = child.getHeight();
    }

    if (!isAnimating.get())
    {
      if (dyConsumed >= HeaderHalfBarBehavior.MINIMUM_SCROLL_DELTA && (layoutParams.height > HeaderHalfBarBehavior.IMAGE_MINIMUM_HEIGHT + toolbar.getHeight() || layoutParams.height == LayoutParams.WRAP_CONTENT))
      {
        final ToggleHeightColorAnimation toggleHeightColorAnimation = new ToggleHeightColorAnimation(child, toolbar.getHeight(), maxHeight, false, HeaderHalfBarBehavior.ANIMATION_DURATION, this);
        child.startAnimation(toggleHeightColorAnimation);
      }
      else if (dyConsumed <= HeaderHalfBarBehavior.MINIMUM_SCROLL_DELTA && layoutParams.height < maxHeight + toolbar.getHeight() && layoutParams.height != LayoutParams.WRAP_CONTENT)
      {
        final ToggleHeightColorAnimation toggleHeightColorAnimation = new ToggleHeightColorAnimation(child, toolbar.getHeight(), maxHeight, true, HeaderHalfBarBehavior.ANIMATION_DURATION, this);
        child.startAnimation(toggleHeightColorAnimation);
      }
    }

    // TODO: Add color change
    // TODO: Add fling

    child.setLayoutParams(layoutParams);
  }

  @Override
  public void onAnimationStart(Animation animation)
  {
    isAnimating.set(true);
  }

  @Override
  public void onAnimationEnd(Animation animation)
  {
    isAnimating.set(false);
  }

  @Override
  public void onAnimationRepeat(Animation animation)
  {
  }
}
