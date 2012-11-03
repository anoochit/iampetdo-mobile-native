package net.redlinesoft.app.iampetdo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ItemActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item);

		// check network connection
		if (checkNetworkStatus()) {

			// load admob
			// Create the adView
			AdView adView = new AdView(this, AdSize.BANNER, "a14fdcb18476694");
			// Lookup your LinearLayout assuming it’s been given
			// the attribute android:id="@+id/mainLayout"
			LinearLayout layout = (LinearLayout) findViewById(R.id.mainItemLayout);
			// Add the adView to it
			layout.addView(adView);
			// Initiate a generic request to load it with an ad
			adView.loadAd(new AdRequest());

			ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);
			ImageView imageView2 = (ImageView) findViewById(R.id.imageView2);
			ImageView imageView3 = (ImageView) findViewById(R.id.imageView3);
			ImageView imageView4 = (ImageView) findViewById(R.id.imageView4);

			// get data
			Intent intent = getIntent();
			String itemTitle = intent.getStringExtra("ItemTitle");
			String itemURL = intent.getStringExtra("ItemURL");

			// set title
			setTitle(itemTitle);

			// extract value
			String[] strThumb = itemURL.toString().split(",");

			try {
				new DownloadImageTask((ImageView) findViewById(R.id.imageView1))
						.execute(strThumb[0]);
				new DownloadImageTask((ImageView) findViewById(R.id.imageView2))
						.execute(strThumb[1]);
				new DownloadImageTask((ImageView) findViewById(R.id.imageView3))
						.execute(strThumb[2]);
				new DownloadImageTask((ImageView) findViewById(R.id.imageView4))
						.execute(strThumb[3]);

			} catch (Exception e) {
				Log.d("APP", e.getMessage());
			}
		} else {
			AlertDialog.Builder alertAdb = new AlertDialog.Builder(
					ItemActivity.this);
			alertAdb.setTitle("Error");
			alertAdb.setMessage("No Internet connection, please connect to internet and try again.");
			alertAdb.setNegativeButton("OK", null);
			alertAdb.show();
			Log.d("Network", "No network connection");
		}
	}
	

	public boolean checkNetworkStatus() {
		final ConnectivityManager connMgr = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final android.net.NetworkInfo wifi = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		final android.net.NetworkInfo mobile = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (wifi.isAvailable()) {
			Log.d("Network", "Connect via Wifi");
			return true;
		} else if (mobile.isAvailable()) {
			Log.d("Network", "Connect via Mobile network");
			return true;
		} else {
			Log.d("Network", "No network connection");
			return false;
		}
	}


	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.activity_item, menu);
		return true;

	}
}
