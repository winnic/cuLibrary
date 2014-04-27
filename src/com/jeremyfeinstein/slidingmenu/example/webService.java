package com.jeremyfeinstein.slidingmenu.example;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.jeremyfeinstein.slidingmenu.example.fragments.SearchPanel;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

public class webService {
	private View ouputText;
	private int method=1;// for keywords searching

	public void contextStartBrowerTo_URL(Context context,View v,String url,int searchMethod) {
		this.method=searchMethod;
		if(method==1){
	    	ouputText=(TextView)v;
		}else{
			ouputText=v;
		}
		// Gets the URL from the UI's text field.
		ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			Log.v("testing", "contextStartBrowerTo_URL: network is okay");
		    new DownloadWebpageTask().execute(url);
		} else {
			 if(method==1){
				 ((SearchPanel) ouputText.getContext()).activateProgressBar(false);
				 ((TextView) ouputText).setText("No network connection available.");
	    	 }
	    }
	}

	    public class DownloadWebpageTask extends AsyncTask<String, Void, String> {
	        @Override
	        protected String doInBackground(String... urls) {
	        	Log.v("testing", "doInBackground");
	        	Log.w("testing",urls[0]);
	            // params comes from the execute() call: params[0] is the url.
	            try {
	            	String books="";
	            	if(method==1){
	            		books=keywordsSearch(urls[0]);
	            	}else if(method==2){   	            		
	            		books=bookSearch(urls[0]);
	            	}
	                return books;
	            } catch (IOException e) {
	            	Log.e("testing","DownloadWebpageTask error :"+e);
	                return "Unable to retrieve web page. URL may be invalid.";
	            }
	        }
	        // onPostExecute displays the results of the AsyncTask.
	        @Override
	        protected void onPostExecute(String result) {
	        	 Log.v("testing","!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+result);
	        	 
	        	 if(method==1){
//	 	        	ouputText.setText(result);	
	        		 ((SearchPanel) ouputText.getContext()).activateProgressBar(false);
	        		 if(result!=null){	 	        	
		 				Intent intent_books = new Intent(ouputText.getContext(), Books.class);
		 				intent_books.putExtra("booklist_str", result);
		 				intent_books.putExtra("mPos", 0);
		 				Log.v("testing","going in to Books activity");
		 				ouputText.getContext().startActivity(intent_books);
	        		 }else{
	        			 ((TextView) ouputText).setText("Sorry! Poor Network Connection.");	
	        		 }
	        	 }else{
	        		 ((Books) ouputText.getContext()).activateProgressBar(false);

	        		Intent intent_bookInfo = new Intent(ouputText.getContext(), BookInfo.class);
	        		intent_bookInfo.putExtra("bookInfo", result);
//	 				intent_books.putExtra("mPos", 0);
	 				Log.v("testing","going in to BookInfo activity");
	 				ouputText.getContext().startActivity(intent_bookInfo);
	        	 }

	       }
	    }
	
		public static String keywordsSearch(String myurl) throws IOException {
			try {//!! nerver forget memory leak!!
				Document doc= Jsoup.connect(myurl).ignoreHttpErrors(true).timeout(100000).get();  
				//Log.e("testing",doc.toString());

				Elements Booklist = doc.select("td.briefcitText");
	            String finalStr="";
                
	            for (int i=0; i<Booklist.size();i++){
	                ArrayList<String> book = new ArrayList<String>();
	                Element link2 = Booklist.get(i).select("a[href]").first();
	            	String website_link = link2.attr("abs:href");
	            	book.add(website_link);
	            	finalStr+=website_link+"\n";
	            	Element row = Booklist.get(i);
	            	Elements rowItems = row.select("p");
	            	for(int j=0;j<rowItems.size();j++){
	            		if(j==0){
	            			finalStr+=rowItems.get(j).text()+"\n\n";
	            		}else if(j==1){
	            			finalStr+="(Author)"+rowItems.get(j).text()+"\n";
	            		}else {
	            			finalStr+=rowItems.get(j).text()+"\n";
	            		}
	            		book.add(rowItems.get(j).text());
	            	}
	            	finalStr+="\r\n";
	            }
				
				return finalStr;
				
			}catch (IOException e) { 
				return "Cannot connect to library server. Please check your connection.";
//	            e.printStackTrace(); 
	        } 
		}
		
		public String bookSearch(String myurl) throws IOException {			 
			Document doc= Jsoup.connect(myurl).ignoreHttpErrors(true).timeout(100000).get();
			
			// get bookname and author and img
			Elements div1=doc.select("body div.bibInfoCol1 td.bibInfoData");
			String bookName="";
			if(div1.size()>1){
				bookName=div1.get(0).text();
			}
			String author="";
			if(div1.size()>1){
				author=div1.get(1).text();
			}
			Elements div2=doc.select("body div.bibInfoCol2 img");
			String imgSrc="";
			if(!div2.isEmpty()){
				imgSrc=div2.first().absUrl("src").toString();
			}
			
			//get e-rsc bibResourceEntry
			Elements bibResourceEntry=doc.select("body table.bibResourceBrief td.bibResourceEntry a");
	        String eResources="";
	        String eLink="";
			for(int i=0;i<bibResourceEntry.size();i++){
				if(i%2==0){
		        	eResources+=bibResourceEntry.get(i).text()+";;";
		        	eLink+=bibResourceEntry.get(i).absUrl("href").toString()+";;";
				}
			}

	        //get copys;
	        Elements bibItemsEntry=doc.select("body div.contentLower tr.bibItemsEntry");
	        String copys="";
	        for(int i=0;i<bibItemsEntry.size();i++){
	        	Element aCopy=bibItemsEntry.get(i);
	        	Elements tds=aCopy.select("td");
	        	String tmp=tds.get(2).text();
	        	if(tmp.matches("NOT CHCKD OUT")){
	        		       tmp="AVAILABLE";
	        	}
	        	copys+=tds.get(0).text()+"<"+tds.get(1).text()+"<"+tmp+";;";
	        }
	       
	        
	        
	        Elements div3=doc.select("body div.contentLower td.bibInfoData");
	        String publisher="";
	        if(div3.size()>0){
	        	publisher=div3.get(0).text();
	        }
	        
	        String outputString=imgSrc+"\n"+bookName+"\n"+author+"\n"+publisher+"\n"+eResources+"\n"+copys+"\n"+eLink+"\r\n";
			return outputString; 
		}
}
