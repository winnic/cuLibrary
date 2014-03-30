package com.jeremyfeinstein.slidingmenu.example.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.internal.nineoldandroids.animation.ObjectAnimator;
import com.jeremyfeinstein.slidingmenu.example.R;


public class TimeArrContentFrag extends Fragment implements ActionBar.TabListener{
	
    private boolean useLogo = false;
    private boolean showHomeUp = true;
    private ActionBarSherlock mSherlock;
    
    private TableLayout timeTable;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		Log.v("testing", "TimeArrContentFrag onCreateView ");
		
		ScrollView lines = (ScrollView) inflater.inflate(R.layout.time_table, container, false);

		timeTable=(TableLayout) lines.findViewById(R.id.table1);
		//!! put string array into view-group --> use ArrayAdapter<String>
		ArrayAdapter<String> libNames=new ArrayAdapter<String>(getActivity(), 
				android.R.layout.simple_list_item_1, android.R.id.text1, getResources().getStringArray(R.array.libNames_OpeningHrs));
		ArrayAdapter<String> dayTimeTable=new ArrayAdapter<String>(getActivity(), 
				android.R.layout.simple_list_item_1, android.R.id.text1, getResources().getStringArray(R.array.Mon_FriTimeTable));
		
		int count=libNames.getCount();
		for (int i=0;i<count;i++){
			TableRow row=(TableRow) timeTable.getChildAt(i);
			TextView tmp=(TextView) row.getChildAt(0);
			tmp.setText(libNames.getItem(i));
			tmp=(TextView) row.getChildAt(1);
			tmp.setText(dayTimeTable.getItem(i));
		}
		//Filled content view with day timetable
		Log.v("testing", "Filled content view with day timetable");
		
		setupActionBar_withDayTabs();
		//also, setup tab event that onclick to change rightFrag(timetable) content
		Log.v("testing", "Created action bar");
		
		return lines;
	}

	


	

    ///////////////////////////////////////////////////////////////////////////
    // Action bar and mode
    ///////////////////////////////////////////////////////////////////////////\
	private void setupActionBar_withDayTabs() {
		final ActionBar ab = getSupportActionBar();

        // set defaults for logo & home up
        ab.setDisplayHomeAsUpEnabled(showHomeUp);
        ab.setDisplayUseLogoEnabled(useLogo);
        ab.setTitle(R.string.openingHours);
        ab.setSubtitle("                     "+getString(R.string.libInfo));
        ab.getTitle();
        Log.v("testing", "ab.getTitle()="+ab.getTitle());
       
        // set up tabs nav
        ab.removeAllTabs();
        ab.addTab(ab.newTab().setText("Mon-Friday").setTabListener(this));
        ab.addTab(ab.newTab().setText("Saturday").setTabListener(this));
        ab.addTab(ab.newTab().setText("Sunday").setTabListener(this));

        // set up list nav
       /* ab.setListNavigationCallbacks(ArrayAdapter
                .createFromResource(getActivity().getBaseContext(), R.array.sections,
                        R.layout.sherlock_spinner_dropdown_item),
                new OnNavigationListener() {
                    public boolean onNavigationItemSelected(int itemPosition,
                            long itemId) {
                        // FIXME add proper implementation
                    	rotateRightFrag();
                        return false;
                    }
                });*/

        // default to tab navigation
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	}
	
    protected final ActionBarSherlock getSherlock() {
        if (mSherlock == null) {
            mSherlock = ActionBarSherlock.wrap(getActivity(), ActionBarSherlock.FLAG_DELEGATE);
        }
        return mSherlock;
    }

    public ActionBar getSupportActionBar() {
        return getSherlock().getActionBar();
    }
    
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {   
		//TODO focus
		switch(tab.getPosition()){
			case 0:
				ArrayAdapter<String> Mon_Fri=new ArrayAdapter<String>(getActivity(), 
	    				android.R.layout.simple_list_item_1, android.R.id.text1, getResources().getStringArray(R.array.Mon_FriTimeTable));
				rotateRightFrag(Mon_Fri);
				//!! break is a must, otherwise it will go through the following cases
				break;
			case 1:
				ArrayAdapter<String> Sat=new ArrayAdapter<String>(getActivity(), 
	    				android.R.layout.simple_list_item_1, android.R.id.text1, getResources().getStringArray(R.array.SatTimeTable));
				rotateRightFrag(Sat);
				break;
			case 2:
				ArrayAdapter<String> Sun=new ArrayAdapter<String>(getActivity(), 
	    				android.R.layout.simple_list_item_1, android.R.id.text1, getResources().getStringArray(R.array.SunTimeTable));
				rotateRightFrag(Sun);
				break;
			default:
		        break;
		}
	}
    
    private void rotateRightFrag(ArrayAdapter<String> dayTimeTable) {
    	if (timeTable != null) {
    		int count = timeTable.getChildCount();
    		for (int i=0;i<count;i++){
    			TableRow row=(TableRow) timeTable.getChildAt(i);
    			TextView rightView=(TextView) row.getChildAt(1);
    			rightView.setText(dayTimeTable.getItem(i));
                ObjectAnimator.ofFloat(rightView, "rotationY", 0, 360)
                .setDuration(500).start();
    		}
        }
    }
    ///////////////////////////////


	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		//do nothing
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO blur
	}
	
}