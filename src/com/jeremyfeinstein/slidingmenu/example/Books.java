package com.jeremyfeinstein.slidingmenu.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.view.Window;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.jeremyfeinstein.slidingmenu.example.fragments.SearchPanel;

public class Books extends Activity {
	
	private int mPos = -1;
	private String booklist_str="";
	private List<String> bookList= new ArrayList<String>();
	private List<String> websites= new ArrayList<String>();
	private ActionBarSherlock mSherlock;
	
	private PullToRefreshGridView mPullRefreshGridView;
	private GridView mGridView;
	GridAdapter mAdapter=new GridAdapter();
	private String connErrStr="Cannot connect to library server. Please check your connection.";
	
	
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
			booklist_strTObookList(getIntent().getExtras().getString("booklist_str"));
		}
			
		super.onCreate(savedInstanceState);
		
		setTitle("Searching Result");
		
		if (mPos == -1 && savedInstanceState != null)
			mPos = savedInstanceState.getInt("mPos");

		
		Log.e("testing","mPos = "+mPos);
		
		//GridView gv = (GridView) inflater.inflate(R.layout.list_grid, null);
		setContentView(R.layout.list_grid);
		
		mPullRefreshGridView = (PullToRefreshGridView) findViewById(R.id.booksGrid);
		mGridView = mPullRefreshGridView.getRefreshableView();
		
		//gv.setBackgroundResource(android.R.color.black);
		
		mPullRefreshGridView.setAdapter(mAdapter);
		mPullRefreshGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
//				if (getActivity() == null)
//					return;
				onBookPressed(position,view);
			}			
		});
		
		// Set a listener to be invoked when the list should be refreshed.
		mPullRefreshGridView.setOnRefreshListener(new OnRefreshListener2<GridView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
				Toast.makeText(Books.this, "Pull Down!", Toast.LENGTH_SHORT).show();
				mPullRefreshGridView.onRefreshComplete();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
				String url=SearchPanel.prefix+(SearchPanel.booksReceived+1) +SearchPanel.suffix;
				TextView output=new TextView(mPullRefreshGridView.getContext());
				Log.v("testing", "URL = "+url);
				new GetDataTask().execute(url);
			}

		});
		mPullRefreshGridView.setMode(Mode.PULL_FROM_END);
	}
	
	private void booklist_strTObookList(String string) {
		booklist_str= string;
		Log.w("testing","booklist_str is gotton successfully!");
		
		String[] tmp=booklist_str.split("\\r\\n");

//		websites=new String[bookList.length];
		for(int i = 0;i<tmp.length;i++){
			String A=tmp[i].split("\\n")[0];
			websites.add(A);
			bookList.add(tmp[i].substring(A.length()+1));
		}
		Log.w("testing","bookList.length="+bookList.size());
		SearchPanel.booksReceived+=tmp.length;
		Log.w("testing","bookList.last="+bookList.get(SearchPanel.booksReceived-1).toString());
	}

	private class GetDataTask extends AsyncTask<String, Void, String> {
		
		@Override
		protected String doInBackground(String... urls) {
			String books="";
			try {
				books=webService.keywordsSearch(urls[0]);
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return books;
		}

		@Override
		protected void onPostExecute(String result) {
//			mListItems.addFirst("Added after refresh...");
//			mListItems.addAll(Arrays.asList(result));
//			mAdapter.notifyDataSetChanged();
			if(result.equals(connErrStr)){
				Toast.makeText(Books.this, connErrStr, Toast.LENGTH_SHORT).show();
				mPullRefreshGridView.onRefreshComplete();
				return;
			}
			
			Toast.makeText(Books.this, "Updated", Toast.LENGTH_SHORT).show();
			booklist_strTObookList(result);
			mAdapter.notifyDataSetChanged();
			mPullRefreshGridView.onRefreshComplete();
			
			super.onPostExecute(result);
		}

	}
	
	private class GridAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return bookList.size();
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
			v.setText(bookList.get(position));
			return convertView;
		}
		
	}
	
	public void onBookPressed(int pos,View view) {
		activateProgressBar(true);
		webService connectToUrl=new webService();
		Log.v("testing","onBookPressed pos = "+pos);
		connectToUrl.contextStartBrowerTo_URL(this.getBaseContext(),view,websites.get(pos),2);

	}
	
	public void activateProgressBar(boolean activate){
		mSherlock.setProgressBarIndeterminateVisibility(activate);
	}

}
