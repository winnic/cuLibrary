package com.jeremyfeinstein.slidingmenu.example;

import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.view.Window;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class Books extends Activity {
	
	private int mPos = -1;
	private String booklist_str="";
	private String[] bookList;
	private String[] websites;
	private ActionBarSherlock mSherlock;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// progress dialog
        if (mSherlock == null) {
            mSherlock = ActionBarSherlock.wrap(this, ActionBarSherlock.FLAG_DELEGATE);
        }
        mSherlock.requestFeature((int) Window.FEATURE_INDETERMINATE_PROGRESS);
        mSherlock.setProgressBarIndeterminate(true);
		
		//TODO add search bar on the top
		
		if (getIntent().getExtras() != null) {
			mPos = getIntent().getExtras().getInt("mPos");
			booklist_str= getIntent().getExtras().getString("booklist_str");
			Log.w("testing","booklist_str is gotton successfully!");
			
			bookList=booklist_str.split("\\r\\n");
			Log.w("testing","bookList.length="+bookList.length);

			websites=new String[bookList.length];
			for(int i = 0;i<bookList.length;i++){
				websites[i]=bookList[i].split("\\n")[0];
				bookList[i]=bookList[i].substring(websites[i].length()+1);
			}
		}
			
		super.onCreate(savedInstanceState);
		
		setTitle("Searching Result");
		
		if (mPos == -1 && savedInstanceState != null)
			mPos = savedInstanceState.getInt("mPos");

		
		Log.e("testing","mPos = "+mPos);
		
		//GridView gv = (GridView) inflater.inflate(R.layout.list_grid, null);
		setContentView(R.layout.list_grid);
		
		GridView gv = (GridView) this.findViewById(R.id.booksGrid);
		
		//gv.setBackgroundResource(android.R.color.black);
		gv.setAdapter(new GridAdapter());
		gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
//				if (getActivity() == null)
//					return;
				onBookPressed(position,view);
			}			
		});
	}
	
	private class GridAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return 12;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				Log.e("testing","convertView = null");
				convertView = getLayoutInflater().inflate(R.layout.grid_item, null);
			}
			TextView v = (TextView) convertView.findViewById(R.id.grid_item_text);
			v.setText(bookList[position]);
			return convertView;
		}
		
	}
	
	public void onBookPressed(int pos,View view) {
		activateProgressBar(true);
		webService connectToUrl=new webService();
		Log.v("testing","onBookPressed pos = "+pos);
		connectToUrl.contextStartBrowerTo_URL(this.getBaseContext(),view,websites[pos],2);

	}
	
	public void activateProgressBar(boolean activate){
		mSherlock.setProgressBarIndeterminateVisibility(activate);
	}

}
