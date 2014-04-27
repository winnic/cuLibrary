package com.jeremyfeinstein.slidingmenu.example;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import util.FireMissilesDialogFragment;
import util.ZoomInZoomOut;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

public class BookInfo extends FragmentActivity {
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
			tmp1.setTextColor(getResources().getColor(R.color.white));
			tmp2.setTextColor(getResources().getColor(R.color.white));
			tmp3.setTextColor(getResources().getColor(R.color.white));
			TableRow row=new TableRow(this);
//			View view=LayoutInflater.from(getBaseContext()).inflate(R.drawable.roundedtr, row, false);
//			row.setBackgroundDrawable(getResources().getDrawable(R.drawable.roundedtr));
			row.setBackgroundResource(R.drawable.roundedtr);
			row.addView(tmp1);
			row.addView(tmp2);
			row.addView(tmp3);
//			if(i%2==1){
//				row.setBackgroundColor(Integer.parseInt("1ABC9C", 16)+0xFF000000);
//			}else{
//				row.setBackgroundColor(Integer.parseInt("1dd2af", 16)+0xFF000000);
//			}

			t.addView(row);
			row.setOnTouchListener(new View.OnTouchListener() {
				
				@Override
				public boolean onTouch(View r, MotionEvent ev) {
					if(ev.getAction()==1){
						r.setBackgroundResource(R.drawable.roundedtr);
					}else{
						r.setBackgroundResource(R.drawable.roundedtr_pressed);
					}
					return false;
				}
			});
			row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  try
                  {
                	TableRow thisRow= (TableRow) v;          	
                	TextView lib=(TextView) thisRow.getChildAt(0);
                	String bCode=((String) ((TextView) thisRow.getChildAt(1)).getText()).trim();
                	Log.v("testing","bCode="+bCode+" and .charAt(1)="+bCode.charAt(1));
                	Intent I = new Intent(getBaseContext(), ZoomInZoomOut.class);
	              	I.putExtra("typeOf_floorPlan", "book");
	              	I.putExtra("lib", "ulib");
	              	I.putExtra("floor",Character.toString(bCode.charAt(1)));
                	startActivity(I);
//                	DialogFragment F= new FireMissilesDialogFragment();
//                	Bundle args = new Bundle();
//	        		args.putString("type","img");
//	        	    args.putString("err", "lib="+lib.getText()+" &bCode="+bCode.getText());
//	        		F.setArguments(args);
//                	F.show(getSupportFragmentManager(), "missiles");
                  }catch(Exception e){
                      Log.v("testing","BookInfo onclick error : "+e.getMessage());
                  }
                }
            });
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