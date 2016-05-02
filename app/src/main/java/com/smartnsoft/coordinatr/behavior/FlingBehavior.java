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
public class FlingBehavior
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

    private final AnimationListener animationListener;

    private final View child;

    private final ImageView imageView;

    private final int minHeight;

    private final int maxHeight;

    private final ScrollerCompat scroller;

    public FlingRunnable(View child, ImageView imageView, int minHeight, int maxHeight, ScrollerCompat scroller,
        AnimationListener animationListener)
    {
      this.child = child;
      this.imageView = imageView;
      this.minHeight = minHeight;
      this.maxHeight = maxHeight;
      this.scroller = scroller;
      this.animationListener = animationListener;
    }

    public void run()
    {
      if (child != null && scroller != null && scroller.computeScrollOffset())
      {
        // Fling is still ongoing, we can retrieve data to manipulate the view(s)
        // scroller.getCurrX(); scroller.getCurrY(); scroller.getCurrVelocity();

        // Repeat while the fling is not yet finished : scroller.computeScrollOffset()
        child.postDelayed(this, FlingRunnable.RUNNABLE_PERIOD);
      }
      else if (child != null && !scroller.computeScrollOffset())
      {
        // Fling is over, trigger the maximize animation cause we reached the top
        if (child.getHeight() == minHeight)
        {
          final ToggleHeightColorAnimation toggleHeightColorAnimation = new ToggleHeightColorAnimation(child, imageView, minHeight,
              maxHeight, true, FlingBehavior.ANIMATION_DURATION, animationListener);
          child.startAnimation(toggleHeightColorAnimation);
        }
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

  public FlingBehavior()
  {
    isAnimating = new AtomicBoolean(false);
  }

  public FlingBehavior(Context context, AttributeSet attrs)
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
          imageView.setMinimumHeight(FlingBehavior.IMAGE_MINIMUM_HEIGHT);
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
    // Allow only the vertical scroll events to be tracked
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

    // Check if an animation is not already ongoing
    if (!isAnimating.get())
    {
      // Minimize on scroll down if the child height is at maximum value
      if (dyConsumed >= FlingBehavior.MINIMUM_SCROLL_OFFSET && child.getHeight() == maxHeight)
      {
        final ToggleHeightColorAnimation toggleHeightColorAnimation = new ToggleHeightColorAnimation(child, imageView, minHeight, maxHeight, false, FlingBehavior.ANIMATION_DURATION, this);
        child.startAnimation(toggleHeightColorAnimation);
      }
      // Maximize on scroll up if the child height is at minimum value and the ScrollView (target) is on start position : we use the dyUnconsumed for that purpose
      else if (dyUnconsumed <= -FlingBehavior.MINIMUM_SCROLL_OFFSET && child.getHeight() == minHeight)
      {
        final ToggleHeightColorAnimation toggleHeightColorAnimation = new ToggleHeightColorAnimation(child, imageView, minHeight, maxHeight, true, FlingBehavior.ANIMATION_DURATION, this);
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

    // React only to top fling
    if (velocityY < 0)
    {
      if (scroller == null)
      {
        scroller = ScrollerCompat.create(child.getContext());
      }

      // Start the fling computation based on scroll data
      scroller.fling(0, target.getScrollY(), (int) velocityX, (int) velocityY, 0, 0, 0, child.getTotalScrollRange());

      // Fling just started
      if (scroller.computeScrollOffset() == true)
      {
        flingRunnable = new FlingRunnable(child, imageView, minHeight, maxHeight, scroller, this);
        child.post(flingRunnable);
      }
      // Fling is over
      else
      {
        flingRunnable = null;

        // Trigger the maximize animation cause we reached the top
        if (child.getHeight() == minHeight)
        {
          final ToggleHeightColorAnimation toggleHeightColorAnimation = new ToggleHeightColorAnimation(child, imageView, minHeight, maxHeight, true, FlingBehavior.ANIMATION_DURATION, this);
          child.startAnimation(toggleHeightColorAnimation);
        }
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
