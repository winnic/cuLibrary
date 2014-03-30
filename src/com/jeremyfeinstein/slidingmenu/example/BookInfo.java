package com.jeremyfeinstein.slidingmenu.example;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class BookInfo extends Activity {
	private ImageView imageview;
	private String[] sessions = new String[7];
	private String[] eLinks ;
	
	public void onCreate(Bundle savedInstanceState) {
		
		if (getIntent().getExtras() != null) {
			String bookInfo= getIntent().getExtras().getString("bookInfo");
			
			sessions=bookInfo.split("\\n");
			
			for(int i=0;i<6;i++){
				Log.v("testing",sessions[i]);
			}

		}
			
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_info);
		setTitle(sessions[1]);
		
		//set img
		if(!sessions[0].isEmpty()){
			String imgSrc=sessions[0];
			new getBitmapFromURL().execute(imgSrc);
			imageview = (ImageView)findViewById(R.id.bookImg);
		}else{
			imageview=(ImageView)findViewById(R.id.bookImg);
			((LinearLayout)imageview.getParent()).removeView(imageview);
		}
		
		//set basic info
		TableLayout t = (TableLayout)findViewById(R.id.table1);
		for (int i=0;i<3;i++){
			TableRow row=(TableRow) t.getChildAt(i);
			TextView tmp=(TextView) row.getChildAt(1);
			tmp.setText(sessions[i+1]);
		}
		
		t=(TableLayout)findViewById(R.id.table2);
		String[] copis=sessions[5].split(";;");
		int count=copis.length;
		if(count>0&&copis[0].length()<5){
			count--;
		}
		for (int i=0;i<count;i++){
			TextView tmp1=new TextView(this);
			TextView tmp2=new TextView(this);
			TextView tmp3=new TextView(this);
			String[] cols=copis[i].split("<");
			tmp1.setText(cols[0]);
			tmp2.setText(cols[1]);
			tmp3.setText(cols[2]);
			tmp1.setLayoutParams(new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
			tmp2.setLayoutParams(new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, 2));
			tmp3.setLayoutParams(new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
			TableRow row=new TableRow(this);
			row.addView(tmp1);
			row.addView(tmp2);
			row.addView(tmp3);

			t.addView(row);
		}
		
		eLinks=sessions[6].split(";;");
		String[] tmpArr=sessions[4].split(";;");//why this time mo empty string at the last like the "copies" one.
		count=tmpArr.length;
		if(count>0&&!tmpArr[count-1].isEmpty()){
//			String[] eRes=new String[count];
//			for (int i=0;i<count;i++){
//				eRes[i]=tmpArr[i];
//			}
			ArrayAdapter<String> op=new ArrayAdapter<String>(this, 
					R.layout.center_text_smallsize, android.R.id.text1, tmpArr);
			ListView l = (ListView)findViewById(R.id.eResourceList);
			l.setAdapter(op);
			l.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
					Log.w("testing","e resource position = "+position);       
					String url = eLinks[position];
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(url));
					startActivity(i);
				}

			});
		}

	}

	public class getBitmapFromURL extends AsyncTask<String, Void, Bitmap> {

	    public Bitmap doInBackground(String... src) {
	        try {
		        URL url = new URL(src[0]);
		        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		        connection.setDoInput(true);
		        connection.connect();
		        InputStream input = connection.getInputStream();
		        Bitmap myBitmap = BitmapFactory.decodeStream(input);
		        return myBitmap;
	        } catch (Exception e) {
		        e.printStackTrace();
		        Log.e("Exception",e.getMessage());
		        return null;
	        }
	    }

	    protected void onPostExecute(Bitmap myBitmap) {
			imageview.setImageBitmap(myBitmap);
	    }
	}

}