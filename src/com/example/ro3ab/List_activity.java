package com.example.ro3ab;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Business.ListAdapter;
import Entities.Episode;
import android.app.Activity;
import android.view.Menu;

public class List_activity extends Activity {
	float standardGravity;
	Random rand;
	ProgressBar progressBar;
	ListView mylist;
	ListAdapter adapter;
	AsyncHttpClient client = new AsyncHttpClient();
	String Url = "http://www.gabapp.somee.com/api/Episodes/GetAllEpisode";
	List<Episode> AllEpisodes;
	private ProgressDialog progressDialog;
	MediaController mediacontrol;
	VideoView video;
	MediaPlayer mp;
	String url, url2;
	String mp3url;
	File[] files;
	Cursor musiccursor;
	ArrayList<String> musicfiles, customlist;
	String sd = "/extSdCard/";
	int postion;
	Button search, shuffle, top;
	AutoCompleteTextView auto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_list_activity);
		
		auto = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
		top = (Button) findViewById(R.id.top);
		shuffle = (Button) findViewById(R.id.shuffle);
		top.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mylist.setSelection(0);
			}
		});
		shuffle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				rand = new Random();
				int n = rand.nextInt(AllEpisodes.size()) + 1;
				mylist.setSelection(n);
			}
		});
		mylist = (ListView) findViewById(R.drawable.listview_item);
		showProgress();
		getResurant();
		setListOnlickListeners();

	}

	private void showProgress() {
		// Create a new progress dialog
		progressDialog = new ProgressDialog(List_activity.this);
		// Set the progress dialog to display a horizontal progress bar
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// Set the dialog title to 'Loading...'
		// progressDialog.setTitle("Loading...");
		// Set the dialog message to 'Loading application View, please wait...'
		progressDialog.setMessage("Loading  data, please wait...");
		// This dialog can't be canceled by pressing the back key
		progressDialog.setCancelable(false);
		// This dialog isn't indeterminate
		progressDialog.setIndeterminate(true);
		// Display the progress dialog
		progressDialog.show();
	}

	public void getResurant() {
		try {
			client.get(Url, new AsyncHttpResponseHandler() {
				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					super.onStart();
				}

				@Override
				public void onSuccess(int arg0, String res) {
					super.onSuccess(arg0, res);
					AllEpisodes = new ArrayList<Episode>();
					Gson g = new Gson();
					Type type = new TypeToken<List<Episode>>() {
					}.getType();
					AllEpisodes = g.fromJson(res, type);

				}

				@Override
				public void onFailure(Throwable arg0) {
					Toast.makeText(List_activity.this, "fail",
							Toast.LENGTH_SHORT).show();
					super.onFailure(arg0);
				}

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					super.onFinish();
					adapter = new ListAdapter(List_activity.this, AllEpisodes);
					mylist.setAdapter(adapter);
					auto.setAdapter(adapter);
					progressDialog.hide();
					// pro.dismiss();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setListOnlickListeners() {
		mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (AllEpisodes != null) {
					Intent theintent = new Intent(getApplicationContext(),
							media.class);
					theintent.putExtra("name", AllEpisodes.get(position).Name);
					theintent.putExtra("tracklink",
							AllEpisodes.get(position).Link);
					theintent.putExtra("picture",
							AllEpisodes.get(position).Picture);
					startActivity(theintent);

					try {

					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_list_activity, menu);
		return true;
	}

}
