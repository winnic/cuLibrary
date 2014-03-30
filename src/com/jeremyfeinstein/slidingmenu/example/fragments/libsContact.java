package com.jeremyfeinstein.slidingmenu.example.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.internal.nineoldandroids.animation.ObjectAnimator;
import com.jeremyfeinstein.slidingmenu.example.R;


public class libsContact extends Fragment implements ActionBar.TabListener{
	
    private boolean useLogo = false;
    private boolean showHomeUp = true;
    private ActionBarSherlock mSherlock;
    
    private TableLayout timeTable;
	private int tabId=0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	
		ScrollView lines = (ScrollView) inflater.inflate(R.layout.libs_contact, container, false);

		timeTable=(TableLayout) lines.findViewById(R.id.theTimeTable);
		//!! put string array into view-group --> use ArrayAdapter<String>
		ArrayAdapter<String> libNames=new ArrayAdapter<String>(getActivity(), 
				android.R.layout.simple_list_item_1, android.R.id.text1, getResources().getStringArray(R.array.libNames));
		final String[] mapLinks=getResources().getStringArray(R.array.mapLinks);
		
		int count=timeTable.getChildCount();
		for (int i=0;i<count;i++){
			TableRow row=(TableRow) timeTable.getChildAt(i);
			TextView tmp=(TextView) row.getChildAt(0);
			tmp.setText(libNames.getItem(i));
			tmp=(TextView) row.getChildAt(1);
			tmp.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                  try
                  {
                	TableRow thisRow= (TableRow) v.getParent();          	
                	int pos=Integer.parseInt(getResources().getResourceEntryName(thisRow.getId()).split("_")[1]);
                	Log.v("testing","TableRow onLongClick & pos="+ pos);
                	String latitude = mapLinks[pos].split(",")[0];
                	String longitude = mapLinks[pos].split(",")[1];
//              		double latitude = 22.416851;
//              		double longitude = 114.208964;
              		String label = ((TextView) thisRow.getChildAt(0)).getText().toString();
              		String uriBegin = "geo:" + latitude + "," + longitude;
              		String query = latitude + "," + longitude + "(" + label + ")";
              		String encodedQuery = Uri.encode(query);
              		String uriString = uriBegin + "?q=" + encodedQuery + "&z=22";
              		Uri uri = Uri.parse(uriString);
              		Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
              		startActivity(intent);
                  }catch(Exception e){
                      return false;
                  }
                    return false;
                }
            });
		}
		//Filled content view with day timetable
		Log.v("testing", "Filled content view with locations");
		
		setupActionBar_withDayTabs();
		//also, setup tab event that onclick to change rightFrag(timetable) content
		Log.v("testing", "Created action bar");
		
		
//		ArrayAdapter<String> mapLinks=new ArrayAdapter<String>(getActivity(), 
//				android.R.layout.simple_list_item_1, android.R.id.text1, getResources().getStringArray(R.array.mapLinks));
//		Log.w("testing", mapLinks.getItem(1));
		
		

		
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
        ab.setTitle(R.string.libContact);
        ab.setSubtitle("                     "+getString(R.string.libInfo));
        ab.getTitle();
        Log.v("testing", "ab.getTitle()="+ab.getTitle());
       
        // set up tabs nav
        ab.removeAllTabs();
        ab.addTab(ab.newTab().setText("Location").setTabListener(this));
        ab.addTab(ab.newTab().setText("Phone").setTabListener(this));
        ab.addTab(ab.newTab().setText("Website").setTabListener(this));

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
		Log.v("testing","tab id ="+tab.getPosition());
		//TODO focus
		switch(tab.getPosition()){
			case 0:
				tabId=0;
				rotateRightFrag(new ArrayAdapter<String>(getActivity(), 
	    				android.R.layout.simple_list_item_1, android.R.id.text1, getResources().getStringArray(R.array.libLocations)));
				//!! break is a must, otherwise it will go through the following cases
				break;
			case 1:
				tabId=1;
				rotateRightFrag(new ArrayAdapter<String>(getActivity(), 
	    				android.R.layout.simple_list_item_1, android.R.id.text1, getResources().getStringArray(R.array.libPhones)));
				break;
			case 2:
				tabId=2;
				rotateRightFrag(new ArrayAdapter<String>(getActivity(), 
	    				android.R.layout.simple_list_item_1, android.R.id.text1, getResources().getStringArray(R.array.website)));
				break;
			default:
		        break;
		}
	}
    
    private void rotateRightFrag(ArrayAdapter<String> dayTimeTable) {
    	if (timeTable != null) {
    		int count = timeTable.getChildCount();
    		Log.v("testing","dayTimeTable.getItem(0) ="+dayTimeTable.getItem(0));
    		for (int i=0;i<count;i++){
    			TableRow row=(TableRow) timeTable.getChildAt(i);
    			final TextView rightView=(TextView) row.getChildAt(1);
    			
    			rightView.setClickable(true);
				
    			if(tabId==0){
    				rightView.setText(Html.fromHtml(dayTimeTable.getItem(i)));

    			}else if(tabId==1){	
    				rightView.setText(dayTimeTable.getItem(i));
	    			Linkify.addLinks(rightView, Linkify.PHONE_NUMBERS);//!! Linkify will also auto-add onclick listener to call intent
    			}else{
	    			rightView.setMovementMethod(LinkMovementMethod.getInstance());
	    			rightView.setText(Html.fromHtml(dayTimeTable.getItem(i)));
    			}

                ObjectAnimator.ofFloat(rightView, "rotationY", 0, 360)
                .setDuration(500).start();
    		}
        }
    }
    ///////////////////////////////

    public void rightViewOnClick(View viewIn) {
    	TextView rightView=(TextView) viewIn;
    	if(tabId!=1){
    		return;
    	}
        try {
        	Intent callIntent = new Intent(Intent.ACTION_CALL);
        	callIntent.setData(Uri.parse("tel:+"+rightView.getText().toString().trim()));
        	startActivity(callIntent );
        } catch (Exception except) {
            Log.e("testing","rightView onclick exception");
        }
    }

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		//do nothing
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO blur
	}
	
}