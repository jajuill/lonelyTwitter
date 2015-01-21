package ca.ualberta.cs.lonelytwitter;

/*
onCreate, onStart, on Resume  (all must be called in order to start app)
onPause, on Stop, on Destroy (all must be called to stop app)
onPause, on Stop, on Destroy (all must be called to stop app)
make sure to SAVE whenever the user makes a change!
in onStart - put all loading data

ListView widget - used to show the android user things that are in our ArrayList
ListView widget - uses arrayAdapter to get info from ArrayList to display on ListView
ArrayLost - add, clear
ListView - fonts, colours, clicking things
ArrayAdapter - know when data changes

Serialization - turning something from a live data file to something that can be transferred... like a file formal
We use GSON - saves a data structure to a file

right click on the project / build path / add build / libraries / and then go from there to add downloaded libraries to eclipse

I UNIX there are 3 ways to open a file
1.to read 2.to write 3.to append  using write overwrites everything!!!
*/




import java.io.BufferedReader;
import java.io.FileInputStream;  
import java.io.FileNotFoundException;  
import java.io.FileOutputStream;  
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.lang.reflect.Type;
import java.io.OutputStreamWriter;


public class LonelyTwitterActivity extends Activity {

	private static final String FILENAME = "file.sav";
	private EditText bodyText;
	private ListView oldTweetsList;
	private ArrayList<String> tweets;//attribute, somehow needed to update things instantly (also need to become array list to change size of array)
	private ArrayAdapter<String> adapter;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main); //runs lines from main.xml (in layout folder)

		bodyText = (EditText) findViewById(R.id.body);  //contacts body widget
		Button saveButton = (Button) findViewById(R.id.save);  //id.save contacts button widget?
		oldTweetsList = (ListView) findViewById(R.id.oldTweetsList);

		saveButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) { //runs when click SAVE button
				setResult(RESULT_OK);
				String text = bodyText.getText().toString();
				tweets.add(text);
				adapter.notifyDataSetChanged();// added to allow change of info
				saveInFile(text, new Date(System.currentTimeMillis()));//right now, stuff is saved to file, but tweets are not displyed 'till app is opened again.
				//finish();//used to close an activity,  got rid of this to stop app from closing on button press

			}
		});
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		tweets = loadFromFile();  //doesn't need type since is attribute
		
		//Create a new ArrayAdapter and connect it to both the array and the widget
		adapter = new ArrayAdapter<String>(this,
				R.layout.list_item, tweets);
		oldTweetsList.setAdapter(adapter);
	}

	private ArrayList<String> loadFromFile() {//now same type as declaired above
		Gson gson = new Gson();
		ArrayList<String> tweets = new ArrayList<String>();
		try {
			FileInputStream fis = openFileInput(FILENAME);
			// https://sites.google.com/site/gson/gson-user-guide 2015-01-21
			Type dataType = new TypeToken<ArrayList<String>>() {}.getType(); //inheriting from Type, taking no arguments - empty () - taking no changes - empty {} We use this class briefly and this instance and then throw it away
			InputStreamReader isr = new InputStreamReader(fis);
			tweets = gson.fromJson(isr, dataType);
			fis.close();
			/*
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));
			String line = in.readLine();
			while (line != null) {//reading loop that looks for end of file
				tweets.add(line);
				line = in.readLine(); */
			

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tweets;
	}
	
	private void saveInFile(String text, Date date) {
		Gson gson = new Gson();
		try {
			FileOutputStream fos = openFileOutput(FILENAME,
					0); //deletes everything in file since write is used by default
			OutputStreamWriter osw = new OutputStreamWriter(fos); //ADDED FOR GSON
			gson.toJson(tweets, osw);//does this work???
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}