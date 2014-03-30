package com.jeremyfeinstein.slidingmenu.example;

import java.io.FileNotFoundException; 
import java.io.IOException; 
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
  
import org.jsoup.Jsoup; 
import org.jsoup.nodes.Document; 
import org.jsoup.nodes.Element; 
import org.jsoup.select.Elements; 
  
  
public class Search { 
	
	private static List<List<String>> final_bookList = new ArrayList<List<String>>();
	/** 
     * @param args 
     * @param tmp 
     * @throws FileNotFoundException  
     */
	//private static Object[] tmp;
    public static void main(String[] args) throws FileNotFoundException { 
          
        String link="http://m.library.cuhk.edu.hk/search~S15/?searchtype=Y&searcharg=web+security&searchscope=15&SORT=DZ&extended=0&SUBMIT=Search&searchlimits=&searchorigarg=Yweb+security%26SORT%3DDZ"; 
          
        try { 
            //connect to the link, doc is the whole html string of the given link 
            Document doc= Jsoup.connect(link).ignoreHttpErrors(true).timeout(100000).get();  
            //from the bookList_Table, find out all books in this tables  
            Elements Booklist = doc.select("td.briefcitText");
                    
            for (int i=0; i<Booklist.size();i++){
                ArrayList<String> book = new ArrayList<String>();
            	System.out.println("row "+i);
            	Element link2 = Booklist.select("a[href]").get(i);
            	String website_link = link2.attr("abs:href");
            	book.add(website_link);
            	System.out.println(website_link);
            	Element row = Booklist.get(i);
            	//Elements website_link = row.select("a:href");
            	Elements rowItems = row.select("p");
            	for(int j=0;j<rowItems.size();j++){
            		System.out.println(rowItems.get(j).text());
            		book.add(rowItems.get(j).text());
            	}
            	final_bookList.add(book);
            }
            
            //!!!!!!!!!!!!!!!  final_bookList is the output of the 12 books now
            //TODO dig out the useful information for a sepecific book, eg. the link below
            String sepcific_Book_Link="http://m.library.cuhk.edu.hk/search~S15?/Yweb+security&searchscope=15&SORT=DZ/Yweb+security&searchscope=15&SORT=DZ&SUBKEY=web+security/1%2C2309%2C2309%2CB/frameset&FF=Yweb+security&searchscope=15&SORT=DZ&1%2C1%2C";

            
            //TODO  after u succeed to generate a good json output, give this class to me. I will use it as my example but not using hardcode anymore 

            //TODO  after that, build it into a responsive program that handle all possible input -----> right output 
            // this part is quite easy, just need to change the "link string" declared on top. The change is depends on your observation 
              
              
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
    } 
  
}