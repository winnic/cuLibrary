package com.jeremyfeinstein.slidingmenu.example.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.jeremyfeinstein.slidingmenu.example.BaseActivity;
import com.jeremyfeinstein.slidingmenu.example.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class TimeARR extends BaseActivity {
	
	private Fragment mContent;
	
	public TimeARR() {
		super(R.string.openingHours);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.v("testing", "TimeARR onCreate ");
		
		// set the Above View
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
		if (mContent == null)
			mContent = new TimeArrContentFrag();	
		
		// set the Above View
		setContentView(R.layout.content_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, mContent)
		.commit();
		
		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, new TimeArrMenuFragment())
		.commit();
		
		// customize the SlidingMenu
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}
	
//	public void switchContent(Fragment fragment) {
//		mContent = fragment;
//		getSupportFragmentManager()
//		.beginTransaction()
//		.replace(R.id.content_frame, fragment)
//		.commit();
//		getSlidingMenu().showContent();
//	}
	public void switchContent(Fragment fragment) {
		if (fragment != null){
			Log.v("testing","switchContent fragment!=null");
			mContent = fragment;	
		}else{
			Log.v("testing","switchContent fragment = null");
			getSlidingMenu().showContent();
			return;
		}
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, fragment)
		.commit();
		getSlidingMenu().showContent();
	}

}
