package com.jeremyfeinstein.slidingmenu.example.fragments;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import util.ZoomInZoomOut;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.jeremyfeinstein.slidingmenu.example.R;
import com.jeremyfeinstein.slidingmenu.example.webService;


public class availComContentFrag extends Fragment implements ActionBar.TabListener{
	
    private boolean useLogo = false;
    private boolean showHomeUp = true;
    private ActionBarSherlock mSherlock;
    
    private String[] floors;
    
	private PullToRefreshGridView mPullRefreshGridView;
	private GridView mGridView;
    
//    private String ip="http://123.255.105.52";
    private String ip="http://localhost";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final ActionBar ab = getSupportActionBar();
        
        // set defaults for logo & home up
        ab.setDisplayHomeAsUpEnabled(showHomeUp);
        ab.setDisplayUseLogoEnabled(useLogo);
        ab.setTitle(R.string.available_computers);
        ab.setSubtitle("                     CU Library");
		
		Log.v("testing", "availComContentFrag onCreateView ");
		
		mPullRefreshGridView = (PullToRefreshGridView) inflater.inflate(R.layout.list_grid, container, false);
		
		mPullRefreshGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
//				if (getActivity() == null)
//					return;
				try
                {
					String floor=floors[position].split("\r\n")[0];
	              	Intent I = new Intent(getActivity(), ZoomInZoomOut.class);
	              	I.putExtra("typeOf_floorPlan", "computer");
	              	I.putExtra("lib", "ulib");
	              	I.putExtra("floor", floor);
	              	startActivity(I);
                }catch(Exception e){
                    Log.v("testing","availComContentFrag onItemClick error : "+e.getMessage());
                }
			}		
		});

		//webservice 
		//input /cuhklib/checkLogIn.php?action=getComputersByLib&lib=uLib
		//output={lib:uLib,computers:[{lib:uLib,floor:LG,id:0,avail=1;}...]}
		String response="{\"query\":\"Pizza\",\"locations\":[94043,90210]}";
		activateProgressBar(true);
		new Web().execute(ip+"/cuhklib/checkLogIn.php?action=getComputersByLib&lib=uLib");
		
		return mPullRefreshGridView;
	}
	
	private class GridAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return floors.length;
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
				convertView = getActivity().getLayoutInflater().inflate(R.layout.grid_item, null);
			}
			TextView v = (TextView) convertView.findViewById(R.id.grid_item_text);
			Spannable span = new SpannableString(floors[position]);
			int length =floors[position].split("\r\n")[0].length();
			span.setSpan(new RelativeSizeSpan(2f), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			length=floors[position].split(":")[0].length();
			span.setSpan(new RelativeSizeSpan(2f), length+1, floors[position].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			v.setText(span);
			return convertView;
		}
		
	}
	
	private class Web extends AsyncTask<String, Void, String>{
		@Override
		protected String doInBackground(String... urls) {Log.v("testing", "url= "+urls[0]);
			String response="";
			HttpClient Client = new DefaultHttpClient();		
			HttpGet httpget = new HttpGet(urls[0]);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			try {
				response = Client.execute(httpget, responseHandler);
			} catch (ClientProtocolException e) {
				return e.getMessage();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return e.getMessage();
			} 
			return response;
		}

		protected void onPostExecute(String response) {
			if(response.matches("Connection to [^\\ ]+ refused")){
				response="[{\"floor\":\"GF\",\"count\":14,\"avail\":8},{\"floor\":\"LG\",\"count\":54,\"avail\":25},{\"floor\":\"1F\",\"count\":4,\"avail\":2},{\"floor\":\"4F\",\"count\":4,\"avail\":2},{\"floor\":\"3F\",\"count\":4,\"avail\":2},{\"floor\":\"2F\",\"count\":4,\"avail\":2}]";
			}
			Log.v("testing","availComContentFrag Web response = "+response);
			JSONArray computers=null;
			try {
				computers = new JSONArray(response);
				floors=new String[computers.length()];
				for(int i=0; i<computers.length();i++){
					JSONObject thisFloor=computers.getJSONObject(i);
					Log.v("testing","floor = "+thisFloor.get("floor"));
					floors[i]=thisFloor.get("floor").toString()+"\r\n\r\nThere are "+thisFloor.get("count")+" computers.\r\n\r\nNumber of computer avail : "+thisFloor.get("avail");
				}
				mPullRefreshGridView.setAdapter(new GridAdapter());
				activateProgressBar(false);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			
//			if (convertView == null) {
//				Log.e("testing","convertView = null");
//				convertView = getActivity().getLayoutInflater().inflate(R.layout.list_grid_twosided, null);
//			}
////			inflater.inflate(R.layout.list_grid_twosided, container, false);
//			TextView t1 = (TextView) convertView.findViewById(R.id.t1);
//			TextView t2 = (TextView) convertView.findViewById(R.id.t2);
//			String tmp1=floors[position].split("\r\n")[0];
//			String tmp2=floors[position].substring(floors[position].split("\r\n")[0].length());
//			t1.setText(tmp1);
////			span.setSpan(new RelativeSizeSpan(2f), 0, tmp1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//			int length=tmp2.split(":")[0].length();
//			Spannable span = new SpannableString( tmp2);
//			span.setSpan(new RelativeSizeSpan(2f), length+1, tmp2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//			t2.setText(span);
//			return convertView;
		}
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
    
	public void activateProgressBar(boolean activate){
		mSherlock.setProgressBarIndeterminateVisibility(activate);
	}
    
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {   

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