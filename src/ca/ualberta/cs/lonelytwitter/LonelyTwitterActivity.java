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
*/




import java.io.BufferedReader;
import java.io.FileInputStream;  
import java.io.FileNotFoundException;  
import java.io.FileOutputStream;  
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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
		ArrayList<String> tweets = new ArrayList<String>();
		try {
			FileInputStream fis = openFileInput(FILENAME);
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));
			String line = in.readLine();
			while (line != null) {
				tweets.add(line);
				line = in.readLine();
			}

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
		try {
			FileOutputStream fos = openFileOutput(FILENAME,
					Context.MODE_APPEND);
			fos.write(new String(date.toString() + " | " + text)
					.getBytes());
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