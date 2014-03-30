package com.jeremyfeinstein.slidingmenu.example.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.app.ActionBar;
import com.jeremyfeinstein.slidingmenu.example.R;

public class TimeArrMenuFragment extends ListFragment {
	
    private ActionBarSherlock mSherlock;
    private ActionBar ab;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ab = getSupportActionBar();
		String[] colors = getResources().getStringArray(R.array.libsInfoMenu);
		ArrayAdapter<String> searchMenuAdapter = new ArrayAdapter<String>(getActivity(), 
				android.R.layout.simple_list_item_1, android.R.id.text1, colors);
		setListAdapter(searchMenuAdapter);
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Fragment newContent = null;
		Log.v("testing","ab.getTitle() = "+ab.getTitle()+" and R.string.openingHours ="+R.string.openingHours);
		switch (position) {
		case 0:
			if(ab.getTitle().toString().equals(getString(R.string.openingHours))){
				newContent = null;
			}else{
				newContent = new TimeArrContentFrag();
			}
			break;
		case 1:
			if(ab.getTitle().toString().equals(getString(R.string.libContact))){
				newContent = null;
			}else{
				newContent = new libsContact();
			}
			break;
		}
		TimeARR fca = (TimeARR) getActivity();
		fca.switchContent(newContent);
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

}
