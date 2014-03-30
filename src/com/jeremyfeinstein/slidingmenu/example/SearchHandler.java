package com.jeremyfeinstein.slidingmenu.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

public class SearchHandler {
	private Document doc;

	public SearchHandler(String contentAsString) {
		Log.e("testing","3.1");
		doc = Jsoup.parse(contentAsString);
		Log.e("testing","3.2");
	}

	public List<List<String>> findBooks() throws IOException {
		List<List<String>> final_bookList = new ArrayList<List<String>>();

		Elements Booklist = doc.select("td.briefcitText");
		for (int i=0; i<Booklist.size();i++){
		    ArrayList<String> book = new ArrayList<String>();
			Element link2 = Booklist.select("a[href]").get(i);
			String website_link = link2.attr("abs:href");
			book.add(website_link);
			Element row = Booklist.get(i);
			//Elements website_link = row.select("a:href");
			Elements rowItems = row.select("p");
			for(int j=0;j<rowItems.size();j++){
				book.add(rowItems.get(j).text());
			}
			final_bookList.add(book);
		} 
		Log.e("testing","3.7");
		return final_bookList;
	}

}
