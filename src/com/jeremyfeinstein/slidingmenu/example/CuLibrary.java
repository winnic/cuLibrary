package com.jeremyfeinstein.slidingmenu.example;

import java.net.URLEncoder;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.text.Html;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.example.fragments.SearchPanel;
import com.jeremyfeinstein.slidingmenu.example.fragments.TimeARR;

public class CuLibrary extends SherlockPreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.app_name);
		
//		Crittercism.init(getApplicationContext(), "508ab27601ed857a20000003");
		this.addPreferencesFromResource(R.xml.main);
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen screen, Preference pref) {
		Class<?> cls = null;
		String title = pref.getTitle().toString();
		if (title.equals(getString(R.string.libInfo))) {
			cls = TimeARR.class;
		} else if (title.equals(getString(R.string.myLib_Record))) {
			return false;
		} else if (title.equals(getString(R.string.rmBooking))) {
			return false;
		} else if (title.equals(getString(R.string.catalogeSearch))) {
			cls = SearchPanel.class;
		} else if (title.equals(getString(R.string.mobileResrc))) {
			return false;
		} else if(title.equals(getString(R.string.news_events))){
			return false;
		}
		Intent intent = new Intent(this, cls);
		startActivity(intent);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
//		case R.id.github:
//			Util.goToGitHub(this);
//			return true;
		case R.id.about:
			new AlertDialog.Builder(this)
			.setTitle(R.string.about)
			.setMessage(Html.fromHtml(getString(R.string.about_msg)))
			.show();
			break;
//		case R.id.licenses:
//			new AlertDialog.Builder(this)
//			.setTitle(R.string.licenses)
//			.setMessage(Html.fromHtml(getString(R.string.apache_license)))
//			.show();
//			break;
		case R.id.contact:
			final Intent email = new Intent(android.content.Intent.ACTION_SENDTO);
			String uriText = "mailto:winnic1116@gmail.com" +
					"?subject=" + URLEncoder.encode("CUHK Library Mobile App Feedback"); 
			email.setData(Uri.parse(uriText));
			try {
				startActivity(email);
			} catch (Exception e) {
				Toast.makeText(this, R.string.no_email, Toast.LENGTH_SHORT).show();
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.example_list, menu);
		return true;
	}
	
}
