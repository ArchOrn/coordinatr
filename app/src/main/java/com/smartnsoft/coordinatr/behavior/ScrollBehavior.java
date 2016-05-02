package com.smartnsoft.coordinatr.behavior;

import java.util.concurrent.atomic.AtomicBoolean;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ScrollerCompat;
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
public class ScrollBehavior
    extends AppBarLayout.Behavior
    implements AnimationListener
{

  private static class ToggleHeightColorAnimation
      extends Animation
  {

    private final View view;

    private final ImageView imageView;

    private final int minHeight;

    private final int maxHeight;

    private final boolean expand;

    public ToggleHeightColorAnimation(View view, ImageView imageView, int minHeight, int maxHeight, boolean expand,
        int duration, AnimationListener animationListener)
    {
      this.view = view;
      this.imageView = imageView;
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
      layoutParams.height = (int) (expand == true ? (interpolatedTime == 1 ? ViewGroup.LayoutParams.WRAP_CONTENT : minHeight + (maxHeight - minHeight) * interpolatedTime) : maxHeight - ((maxHeight - minHeight) * interpolatedTime));
      view.setLayoutParams(layoutParams);
      imageView.setAlpha(expand == true ? interpolatedTime : 1.f - interpolatedTime);
    }

    @Override
    public boolean willChangeBounds()
    {
      return true;
    }
  }

  public static class FlingRunnable
      implements Runnable
  {

    public static final int RUNNABLE_PERIOD = 2;

    private final View child;

    private final ScrollerCompat scroller;

    public FlingRunnable(View child, ScrollerCompat scroller)
    {
      this.child = child;
      this.scroller = scroller;
    }

    public void run()
    {
      if (child != null && scroller != null && scroller.computeScrollOffset() == true)
      {
        // The fling is still ongoing, we can retrieve data to manipulate the view(s)
        // scroller.getCurrX(); scroller.getCurrY(); scroller.getCurrVelocity();

        // Do it while the fling is not yet finished
        // scroller.computeScrollOffset() == false
        child.postDelayed(this, FlingRunnable.RUNNABLE_PERIOD);
      }
      else if (child != null)
      {
        // The fling is finished, we can trigger an animation if we reach the top for example
      }
    }
  }

  private static final int IMAGE_MINIMUM_HEIGHT = 200;

  private static final int ANIMATION_DURATION = 400;

  private static final int MINIMUM_SCROLL_OFFSET = 10;

  private Toolbar toolbar;

  private ImageView imageView;

  private int maxHeight;

  private int minHeight;

  private AtomicBoolean isAnimating;

  private ScrollerCompat scroller;

  private Runnable flingRunnable;

  public ScrollBehavior()
  {
    isAnimating = new AtomicBoolean(false);
  }

  public ScrollBehavior(Context context, AttributeSet attrs)
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
          imageView.setMinimumHeight(ScrollBehavior.IMAGE_MINIMUM_HEIGHT);
        }

        if (childAt instanceof Toolbar)
        {
          toolbar = (Toolbar) childAt;
        }
      }
    }

    if (maxHeight == 0)
    {
      maxHeight = child.getHeight();
    }

    if (minHeight == 0 && toolbar != null)
    {
      minHeight = toolbar.getHeight();
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

    if (!isAnimating.get())
    {
      if (dyConsumed >= ScrollBehavior.MINIMUM_SCROLL_OFFSET && child.getHeight() == maxHeight)
      {
        final ToggleHeightColorAnimation toggleHeightColorAnimation = new ToggleHeightColorAnimation(child, imageView, toolbar.getHeight(), maxHeight, false, ScrollBehavior.ANIMATION_DURATION, this);
        child.startAnimation(toggleHeightColorAnimation);
      }
      else if (dyConsumed <= -ScrollBehavior.MINIMUM_SCROLL_OFFSET && child.getHeight() == toolbar.getHeight())
      {
        final ToggleHeightColorAnimation toggleHeightColorAnimation = new ToggleHeightColorAnimation(child, imageView, toolbar.getHeight(), maxHeight, true, ScrollBehavior.ANIMATION_DURATION, this);
        child.startAnimation(toggleHeightColorAnimation);
      }
    }
  }

  @Override
  public boolean onNestedFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX,
      float velocityY, boolean consumed)
  {
    final boolean result = super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);

    // Clear the old runnable if a new fling is triggered
    if (flingRunnable != null)
    {
      child.removeCallbacks(flingRunnable);
    }

    // Only if flinging top
    if (velocityY < 0)
    {
      if (scroller == null)
      {
        scroller = ScrollerCompat.create(child.getContext());
      }

      // Start the fling computation based on scroll data
      // scroller.fling(0, totalYScrolled, (int) velocityX, (int) velocityY, 0, 0, 0, child.getTotalScrollRange());

      // Fling just started
      if (scroller.computeScrollOffset() == true)
      {
        flingRunnable = new FlingRunnable(child, scroller);
        child.post(flingRunnable);
      }
      // Fling is over
      else
      {
        flingRunnable = null;

        // The fling is finished, we can trigger an animation if we reach the top for example
      }
    }

    return result;
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
