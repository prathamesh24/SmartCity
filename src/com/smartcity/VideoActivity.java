package com.smartcity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class VideoActivity extends Activity {

	WebView youtube;
	String id, summary;
	ProgressBar progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_video);
		youtube = (WebView) findViewById(R.id.youtubeView);
		progress = (ProgressBar) findViewById(R.id.progress);

		id = getIntent().getExtras().getString("youtubeID");

		summary = "https://www.youtube.com/embed/" + id + "?modestbranding=1&showinfo=0&rel=0&fs=0";

		youtube.getSettings().setJavaScriptEnabled(true);
		youtube.getSettings().setPluginState(WebSettings.PluginState.ON);
		youtube.getSettings().setUserAgentString("");
		youtube.setScrollbarFadingEnabled(false);
		youtube.setVerticalScrollBarEnabled(false);
		youtube.setHorizontalScrollBarEnabled(false);

		youtube.setWebChromeClient(new WebChromeClient() {
		});
		youtube.setWebViewClient(new HelloWebViewClient());
		youtube.loadUrl(summary);

	}

	private class HelloWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return false;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);

			progress.setVisibility(View.GONE);
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		youtube.stopLoading();
		youtube.onPause();
		finish();
	}

}
