package com.jeremyfeinstein.slidingmenu.example.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.jeremyfeinstein.slidingmenu.example.BaseActivity;
import com.jeremyfeinstein.slidingmenu.example.R;
import com.jeremyfeinstein.slidingmenu.example.webService;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class SearchPanel extends BaseActivity {
	
	private Fragment mContent;
	public static int booksReceived=0;
	public static String prefix="";
	public static String suffix=",858,858,B/browse";
	private InputMethodManager imm;
	private Menu menu;
	
	public SearchPanel() {
		super(R.string.catalogeSearch);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// progress dialog
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setSupportProgressBarIndeterminate(true);
		
		super.onCreate(savedInstanceState);
		imm= (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		booksReceived=0;
		
		// set the Above View
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
		if (mContent == null)
			mContent = new SearchContentFragment();	
		
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
		.replace(R.id.menu_frame, new SearchMenuFragment())
		.commit();
		
		// customize the SlidingMenu
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		this.menu=menu;
    	//!!
        menu.add("Search")
            .setIcon(R.drawable.ic_search)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if(item.getTitle()=="Search") {
        	booksReceived=0;
        	
        	EditText keyWords=(EditText) mContent.getView().findViewById(R.id.keyWords);
        	TextView output=(TextView)(mContent.getView().findViewById(R.id.output));
        	output.setText("");
        	activateProgressBar(true);
			String tmp=keyWords.getText().toString();
			if(tmp.equals("")){
				output.setText("Search keywords are needed to search related books.");
				activateProgressBar(false);
				imm.hideSoftInputFromWindow(keyWords.getWindowToken(), 0);
				return false;
			}
			String[] words=tmp.split(" ");
			String searchKeyWords="";
			for(int i=0;i<words.length-1;i++){
				searchKeyWords+=words[i]+"+";
			}
			searchKeyWords+=words[words.length-1];
			Log.v("testing", "searchKeyWords ="+searchKeyWords);
 
//			String url="http://m.library.cuhk.edu.hk/search/?searchtype=Y&searcharg="+searchKeyWords+"&searchscope=15";
			
			prefix="http://m.library.cuhk.edu.hk/search~S15?/Y" +
					searchKeyWords +
					"&searchscope=15&SORT=D/Y" +
					searchKeyWords +
					"&SUBKEY=" +
					searchKeyWords +
					"/";
			String url=prefix+(booksReceived+1) +suffix;
					
			Log.v("testing", "URL = "+url);
			webService connectToUrl=new webService();
			connectToUrl.contextStartBrowerTo_URL(this.getBaseContext(),output,url,1);
			imm.hideSoftInputFromWindow(keyWords.getWindowToken(), 0);
			Log.v("testing", "output succeeds");
        	return true;
        }else{
        	return super.onOptionsItemSelected(item);
        }
    }
	
	public void switchContent(Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, fragment)
		.commit();
		getSlidingMenu().showContent();
	}
	
	public void activateProgressBar(boolean activate){
	    setSupportProgressBarIndeterminateVisibility(activate);
	}

}
