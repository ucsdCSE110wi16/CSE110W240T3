package com.vorph.anim;

import android.content.Context;
import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

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

		// Stop outside instantiation with just default constructor.
		private AnimBuilder() {}
	}
	
	
}
