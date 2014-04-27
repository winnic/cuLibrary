package com.jeremyfeinstein.slidingmenu.example.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.example.R;
import com.jeremyfeinstein.slidingmenu.example.webService;

public class SearchContentFragment extends Fragment{
	
	private LinearLayout lines;
    private Spinner cuhkLibs_spinner;
//    private Button searchBtn;
	private EditText keyWords;
    
    private TextToSpeech tts;
    private ImageView speakButton;    
    private static final int REQUEST_CODE = 1234;
    private ArrayList<String> matches;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		lines= (LinearLayout) inflater.inflate(R.layout.spinner, container, false);
//		lines.setBackgroundResource(android.R.color.white);
		Log.v("testing", "SearchContentFragment onCreateView ");

		cuhkLibs_spinner=(Spinner) lines.findViewById(R.id.cuhkLibs_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(),
				R.array.cuhkLibs, R.layout.sherlock_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cuhkLibs_spinner.setAdapter(adapter);
       
		final TextView output=(TextView) lines.findViewById(R.id.output);
//		output.setMovementMethod(new ScrollingMovementMethod());
		
		
		keyWords=(EditText) lines.findViewById(R.id.keyWords);
		
		/////////////////////////////////////////////////
		setupTts_to_setSelectedText();
		/////////////////////////////////////////////////
		
		

		Log.v("testing", "SearchContentFragment onCreateView ends ");
		return lines;
	}
	
	
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
    	for(int i=0;i<matches.size();i++){
    		menu.add(matches.get(i));
    	}
    }

    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
    	keyWords.setText(item.toString(),TextView.BufferType.EDITABLE);
        return true;
    }
	
	
	
	
	////////////////          TTS methods
	
	 private void setupTts_to_setSelectedText() {
		speakButton= (ImageView) lines.findViewById(R.id.speak);
		speakButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
           	 Log.v("testing", "inputButton setOnClickListener");
           	 startVoiceRecognitionActivity();
           }

       });
		speakButton.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent ev) {
				if(ev.getAction()==1){
					speakButton.setImageResource(R.drawable.microphone);
				}else{
					speakButton.setImageResource(R.drawable.microphone2);
				}
				return false;
			}
		});
		
		tts = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
			    @Override
			    public void onInit(int status){
			    	myTtsInit(status);
			    }
			});		
		
		registerForContextMenu(lines);
		lines.setLongClickable(false);
	}

	private void myTtsInit(int status){
		if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("testing", "This Language is not supported");
            }             
        } else {
            Log.e("testing", "Initilization Failed!");
        }
        
        // Disable button if no recognition service is present
        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0)
        {
        	speakButton.setEnabled(false);
//        	inputButton.setText("Recognizer not present");
        } else{
        	 Log.v("testing", "Recognition service is present and num of service = "+activities.size());
        }
	}
	
    /**
     * Fire an intent to start the voice recognition activity.
     */
    private void startVoiceRecognitionActivity()
    {
    	Log.v("testing", "startVoiceRecognitionActivity");
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent, REQUEST_CODE);
    }
    
    /**
     * Handle the results from the voice recognition activity.
     */
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE && resultCode == -1)
        {
            // Populate the wordsList with the String values the recognition engine thought it heard
            matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
   
            lines.showContextMenu();//!! call context menu by method
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
 
	////////////			TTS ends

}