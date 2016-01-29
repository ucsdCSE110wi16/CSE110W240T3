package com.vorph.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.annotation.AnimRes;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toolbar;

import com.vorph.utils.R;

public class AnimUtils {

	static Context context;
	
	public static void init(Context context) {
		AnimUtils.context = context;
	}
	
	public static void fadeIn(final View view) {
//		view.startAnimation(anim(android.R.anim.fade_in));
		AnimBuilder.create(context)
			.view(view)
			.res(android.R.anim.fade_in)
			.duration(200)
			.listener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					view.setVisibility(View.VISIBLE);
				}

				public void onAnimationRepeat(Animation animation) {}
				public void onAnimationEnd(Animation animation) {}
			})
			.start();
	}
	
	public static void fadeOut(final View view) {
		AnimBuilder.create(context)
			.view(view)
			.res(android.R.anim.fade_out)
			.listener(new AnimationListener() {

				public void onAnimationStart(Animation animation) {}
				public void onAnimationRepeat(Animation animation) {}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					view.setVisibility(View.INVISIBLE);
				}
			}).start();
	}

	public static AnimBuilder fadeIn(final Context context) {
		return AnimBuilder.create(context, android.R.anim.fade_in);
	}

	public static AnimBuilder fadeOut(final Context context) {
		return AnimBuilder.create(context, android.R.anim.fade_out);
	}

	public static Animation anim(int intRes) {
		return AnimationUtils.loadAnimation(context, intRes);
	}

	public static final class AnimBuilder {
		
		private int duration = 300,
				    delayOffset = 50,
				    res;

		private AnimationListener listener;
		
		private View view;
		private Context context;
		
		public AnimBuilder context(@NonNull Context context) {
			this.context = context;
			return this;
		}
		
		public static AnimBuilder create(@NonNull Context context) {
			return new AnimBuilder().context(context);
		}
		
		public AnimBuilder listener(@Nullable AnimationListener listener) {
			this.listener = listener;
			return this;
		}
		
		public AnimBuilder res(@AnimRes int res) {
			this.res = res;
			return this;
		}
		
		public AnimBuilder delay(int delay) {
			delayOffset = delay;
			return this;
		}
		
		public AnimBuilder duration(int duration) {
			this.duration = duration;
			return this;
		}

		public AnimBuilder view(@NonNull View view) {
			this.view = view;
			return this;
		}


		public void start() {
			Animation anim = AnimationUtils.loadAnimation(context, res);
			anim.setDuration(duration);
			anim.setStartOffset(delayOffset);
			anim.setAnimationListener(listener);
			view.startAnimation(anim);			  
		}

		private static AnimBuilder create(Context context,
										  @AnimRes int res) {
			return new AnimBuilder().context(context).res(res);
		}

		// Stop outside instantiation with just default constructor.
		private AnimBuilder() {}
	}



	// The following is not used with the above methods
	/************************************* SIMPLER ANIMATIONS ************************************/

	public static Animation getFadeInAnimation() {
		Animation fadeIn = new AlphaAnimation(1, 1); // 0 1
		fadeIn.setInterpolator(new DecelerateInterpolator());
		fadeIn.setDuration(300);
		return fadeIn;
	}

	public static Animation getFadeOutAnimation() {
		Animation fadeOut = new AlphaAnimation(1, 1); // 1 0
		fadeOut.setInterpolator(new AccelerateInterpolator());
		fadeOut.setDuration(300);
		return fadeOut;
	}


//	// TODO Needs some work, don't use
//	@Deprecated
//	public static void animateToolbar(final Toolbar toolbar,
//									  final boolean reveal,
//									  @ColorRes int colorOne,
//									  @ColorRes int colorNon) {
//		Animation toolbarAnim =
//							reveal
//								? getFadeInAnimation()
//								: getFadeOutAnimation();
//		int color = reveal
//				? toolbar.getResources().getColor(colorOne)
//				: toolbar.getResources().getColor(colorNon;
//		animateToolbar(toolbarAnim, toolbar, reveal, color, 300, 0);
//	}
//
//	public static void animateToolbar(final Animation toolAnim,
//									  final Toolbar toolbar,
//									  final boolean reveal,
//									  final int color,
//									  final int duration,
//									  final int delay)
//	{
//		toolbar.setAnimation(toolAnim);
//		toolbar.animate()
//				.setDuration(duration)
//				.setStartDelay(delay)
//				.setListener(new AnimatorListenerAdapter() {
//					@Override
//					public void onAnimationEnd(Animator animation) {
//						super.onAnimationEnd(animation);
//						toolbar.setTitleTextColor(color);
//
//						// TODO Current animate toolbar needs some work on animation colors.
////						toolbar.setBackgroundColor(reveal
////								? toolbar.getResources().getColor(R.color.recyclerBackground)
////								: toolbar.getResources().getColor(R.color.blue_400));
//					}
//				}).start();
//	}

}
