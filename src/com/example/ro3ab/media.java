package com.example.ro3ab;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class media extends Activity {
	private ProgressDialog progressDialog;
	private Button forward,back;
	private ToggleButton plause;
	private ImageView iv;
	MediaPlayer mediaPlayer;
	private double startTime = 0;
	private double finalTime = 0;
	private Handler myHandler = new Handler();;
	private int forwardTime = 5000;
	private int backwardTime = 5000;
	private SeekBar seekbar;
	private TextView tx1, tx2, tx3;
	ProgressBar imgprogress;
	String picture;

	public static int oneTimeOnly = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_media);

		String name = getIntent().getStringExtra("name");
		String tracklink = getIntent().getStringExtra("tracklink");
		picture = getIntent().getStringExtra("picture");

		forward = (Button) findViewById(R.id.button);
		plause = (ToggleButton)findViewById(R.id.toggle);
		back = (Button) findViewById(R.id.button4);
		imgprogress = (ProgressBar) findViewById(R.id.imgageprogress);
		iv = (ImageView) findViewById(R.id.imageView);
		getView();

		tx1 = (TextView) findViewById(R.id.textView2);
		tx2 = (TextView) findViewById(R.id.textView3);
		tx3 = (TextView) findViewById(R.id.textview);
		tx3.setText(name);

		// mediaPlayer = MediaPlayer.create(getApplicationContext(),
		// R.raw.phonicssong2);
		// mediaPlayer.start();
		/*********************************************************************************************************************/
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			mediaPlayer.setDataSource(tracklink);
		} catch (IllegalArgumentException e) {
			Toast.makeText(getApplicationContext(),
					"You might not set the URI correctly!", Toast.LENGTH_LONG)
					.show();
		} catch (SecurityException e) {
			Toast.makeText(getApplicationContext(),
					"You might not set the URI correctly!", Toast.LENGTH_LONG)
					.show();
		} catch (IllegalStateException e) {
			Toast.makeText(getApplicationContext(),
					"You might not set the URI correctly!", Toast.LENGTH_LONG)
					.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			mediaPlayer.prepare();
		} catch (IllegalStateException e) {
			Toast.makeText(getApplicationContext(),
					"You might not set the URI correctly!", Toast.LENGTH_LONG)
					.show();
		} catch (IOException e) {
			Toast.makeText(getApplicationContext(),
					"You might not set the URI correctly!", Toast.LENGTH_LONG)
					.show();
		}
		mediaPlayer.start();
		/********************************************************************************************************************/
		seekbar = (SeekBar) findViewById(R.id.seekBar);

		seekbar.setClickable(true);

		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			int progressChanged;

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				progressChanged = progress;

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				mediaPlayer.seekTo(progressChanged);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

		});
		plause.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean on = ((ToggleButton) v).isChecked();
				if (on) {				
					Toast.makeText(getApplicationContext(), "Pausing sound",
							Toast.LENGTH_SHORT).show();
					mediaPlayer.pause();	
				}else {
					Toast.makeText(getApplicationContext(), "Playing sound",
							Toast.LENGTH_SHORT).show();
					mediaPlayer.start();

					finalTime = mediaPlayer.getDuration();
					startTime = mediaPlayer.getCurrentPosition();

					if (oneTimeOnly == 0) {
						seekbar.setMax((int) finalTime);
						oneTimeOnly = 1;
					}
					tx2.setText(String.format(
							"%d min, %d sec",
							TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
							TimeUnit.MILLISECONDS.toSeconds((long) finalTime)
									- TimeUnit.MINUTES
											.toSeconds(TimeUnit.MILLISECONDS
													.toMinutes((long) finalTime))));

					tx1.setText(String.format(
							"%d min, %d sec",
							TimeUnit.MILLISECONDS.toMinutes((long) startTime),
							TimeUnit.MILLISECONDS.toSeconds((long) startTime)
									- TimeUnit.MINUTES
											.toSeconds(TimeUnit.MILLISECONDS
													.toMinutes((long) startTime))));

					seekbar.setProgress((int) startTime);
					myHandler.postDelayed(UpdateSongTime, 100);
				}
			}
		});

		forward.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int temp = (int) startTime;

				if ((temp + forwardTime) <= finalTime) {
					startTime = startTime + forwardTime;
					mediaPlayer.seekTo((int) startTime);
					Toast.makeText(getApplicationContext(),
							"You have Jumped forward 5 seconds",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(),
							"Cannot jump forward 5 seconds", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int temp = (int) startTime;

				if ((temp - backwardTime) > 0) {
					startTime = startTime - backwardTime;
					mediaPlayer.seekTo((int) startTime);
					Toast.makeText(getApplicationContext(),
							"You have Jumped backward 5 seconds",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(),
							"Cannot jump backward 5 seconds",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	public View getView() {

		Glide.with(getBaseContext()).load(picture)

		.into(new GlideDrawableImageViewTarget(iv) {
			@Override
			public void onResourceReady(GlideDrawable drawable,
					GlideAnimation anim) {
				imgprogress.setVisibility(View.GONE);

				super.onResourceReady(drawable, anim);
			}
		});
		return iv;
	}

	private Runnable UpdateSongTime = new Runnable() {
		@TargetApi(Build.VERSION_CODES.GINGERBREAD)
		public void run() {
			startTime = mediaPlayer.getCurrentPosition();
			tx1.setText(String.format(
					"%d min, %d sec",

					TimeUnit.MILLISECONDS.toMinutes((long) startTime),
					TimeUnit.MILLISECONDS.toSeconds((long) startTime)
							- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
									.toMinutes((long) startTime))));
			seekbar.setProgress((int) startTime);
			myHandler.postDelayed(this, 100);
		}
	};

	// private void showProgress() {
	// //Create a new progress dialog
	// progressDialog = new ProgressDialog(media.this);
	// //Set the progress dialog to display a horizontal progress bar
	// progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	// //Set the dialog title to 'Loading...'
	// // progressDialog.setTitle("Loading...");
	// //Set the dialog message to 'Loading application View, please wait...'
	// progressDialog.setMessage("Loading  data, please wait...");
	// //This dialog can't be canceled by pressing the back key
	// progressDialog.setCancelable(false);
	// //This dialog isn't indeterminate
	// progressDialog.setIndeterminate(true);
	// //Display the progress dialog
	// progressDialog.show();
	// }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_mediaplayer, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
		}
		super.onBackPressed();
	}
}
