package com.mapure.amap.contest.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.mapure.amap.contest.R;
import com.mapure.amap.contest.util.NetworkUtils;
import com.mapure.amap.contest.util.StringUtils;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.api.UsersAPI;
import com.weibo.sdk.android.net.RequestListener;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SNSEditActivity extends Activity {
	public static final String WB_API_KEY = "3736768454";
	
	private final int ADD_ACCOUNT_REQUEST_CODE = 0;

	private String content;

	int textCount;
	int textLimit;

	private EditText mSNSEditText;
	private TextView mSNSPublishLimit;

	public static Oauth2AccessToken accessToken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		setContentView(R.layout.activity_sns_edit);

		getActionBar().setDisplayHomeAsUpEnabled(true);

        content = "#武汉大学120周年校庆#";

		textCount = StringUtils.getWordCount(content);
		textLimit = 100;

		if (NetworkUtils.isNetworkAvailable(this)) {
				SharedPreferences prefs = getApplicationContext()
						.getSharedPreferences("com.mapure.amap.contest",
								Context.MODE_APPEND);
				String token = prefs.getString("weibo_token", null);
				String expires = prefs.getString("weibo_expires", null);
				String uid = prefs.getString("uid", null);

				if ((token == null && expires == null && uid == null)) {
					getActionBar().setTitle(getString(R.string.share_2_weibo));
				} else {
					accessToken = new Oauth2AccessToken(token, expires);
					if (accessToken.isSessionValid()) {
						setActionBarTitle();
					} else {
                        getActionBar().setTitle(getString(R.string.share_2_weibo));
					}
				}

		} else {
                getActionBar().setTitle(getString(R.string.share_2_weibo));
		}

		mSNSEditText = (EditText) findViewById(R.id.sns_edit);
		mSNSPublishLimit = (TextView) findViewById(R.id.sns_publish_limit);
		mSNSPublishLimit.setText(String.valueOf(textLimit - textCount));
		mSNSEditText.setText(content);
		mSNSEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String temp = s.toString();
				textCount = StringUtils.getWordCount(temp);
				if ((textLimit - textCount) >= 0) {
					mSNSPublishLimit
							.setTextColor(R.color.sns_publish_limit_color);
					mSNSPublishLimit.setText(String.valueOf(textLimit
							- textCount));
				} else {
					mSNSPublishLimit.setTextColor(Color.RED);
					mSNSPublishLimit.setText(String.valueOf(textLimit
							- textCount));
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

        ImageView mSharingImage = (ImageView) findViewById(R.id.share_pic);
        mSharingImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
        mSharingImage.setBackgroundResource(R.drawable.sns_publish_pic_bg);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.share_pic_container);
        frameLayout.setVisibility(View.VISIBLE);

	}

    @Override
    protected void onResume() {
        super.onResume();

        Drawable mActionBarBackgroundDrawable = getResources().getDrawable(
                R.drawable.ab_solid_mapure);
        mActionBarBackgroundDrawable.setAlpha(255);
        getActionBar().setBackgroundDrawable(
                mActionBarBackgroundDrawable);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.sns_edit, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ADD_ACCOUNT_REQUEST_CODE && resultCode == RESULT_OK) {
			Crouton.makeText(SNSEditActivity.this, getString(R.string.login_success), Style.INFO)
					.show();

			setActionBarTitle();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_publish:
			if (!NetworkUtils.isNetworkAvailable(this)) {
				Crouton.makeText(this, getString(R.string.check_network),
						Style.ALERT).show();
				return false;
			}

			content = mSNSEditText.getText().toString();

			if ((textLimit - textCount) >= 0) {
				if (content.length() == 0) {
					Crouton.makeText(SNSEditActivity.this,
							getString(R.string.check_content), Style.ALERT).show();
				} else {
						SharedPreferences prefs = getSharedPreferences(
								"com.mapure.amap.contest",
								Context.MODE_APPEND);
						String token = prefs.getString("weibo_token", null);
						String expires = prefs.getString("weibo_expires", null);
						if (token == null && expires == null) {
							Intent intent = new Intent();
							intent.setClass(SNSEditActivity.this,
									WeiboAuthActivity.class);
							startActivityForResult(intent,
									ADD_ACCOUNT_REQUEST_CODE);
						} else {
							accessToken = new Oauth2AccessToken(token, expires);
							if (accessToken.isSessionValid()) {

								StatusesAPI status = new StatusesAPI(
										accessToken);
								status.update(content + " " + getResources().getString(R.string.sns_tail), null, null,
										new RequestListener() {

											@Override
											public void onComplete(String arg0) {
												Handler h = new Handler(
														getMainLooper());

												h.post(new Runnable() {
													@Override
													public void run() {
														Toast.makeText(
																getApplicationContext(),
																getString(R.string.sent_successfully),
																Toast.LENGTH_LONG)
																.show();
													}
												});

												SNSEditActivity.this.finish();
											}

											@Override
											public void onError(
													final WeiboException e) {
												Handler h = new Handler(
														getMainLooper());

												h.post(new Runnable() {
													@Override
													public void run() {
														Crouton.makeText(
																SNSEditActivity.this,
																"Auth error : "
																		+ e.getMessage(),
																Style.ALERT)
																.show();
													}
												});

											}

											@Override
											public void onIOException(
													final IOException e) {

												Handler h = new Handler(
														getMainLooper());

												h.post(new Runnable() {
													@Override
													public void run() {
														Crouton.makeText(
																SNSEditActivity.this,
																"Auth error : "
																		+ e.getMessage(),
																Style.ALERT)
																.show();
													}
												});

											}

										});

							} else {
								Intent intent = new Intent();
								intent.setClass(SNSEditActivity.this,
										WeiboAuthActivity.class);
								startActivityForResult(intent,
										ADD_ACCOUNT_REQUEST_CODE);
							}
						}
				}

			} else {
				Crouton.makeText(SNSEditActivity.this,
						getString(R.string.check_content), Style.ALERT).show();
			}

			return true;
		case android.R.id.home:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

    @Override
    public void onBackPressed() {
        if (textCount == 0) {
            super.onBackPressed();
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle(getString(R.string.cancel_title)).setMessage(getString(R.string.sns_edit_quit_alert)).setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SNSEditActivity.super.onBackPressed();
                }
            }).setNegativeButton(getString(R.string.no), null);
            alertDialog.show();
        }

    }

    private void setActionBarTitle() {
			SharedPreferences prefs = getApplicationContext()
					.getSharedPreferences("com.mapure.amap.contest",
							Context.MODE_APPEND);
			String token = prefs.getString("weibo_token", null);
			String expires = prefs.getString("weibo_expires", null);
			String uid = prefs.getString("uid", null);
			accessToken = new Oauth2AccessToken(token, expires);

			UsersAPI user = new UsersAPI(accessToken);
			user.show(Long.valueOf(uid), new RequestListener() {

				@Override
				public void onComplete(String response) {
					try {
						JSONObject person = new JSONObject(response);
						final String name = person.getString("name");

						Handler h = new Handler(getMainLooper());

						h.post(new Runnable() {
							@Override
							public void run() {
								SNSEditActivity.this.getActionBar()
										.setTitle(name);
							}
						});
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onError(WeiboException e) {
					e.printStackTrace();
				}

				@Override
				public void onIOException(IOException e) {
					e.printStackTrace();
				}

			});

	}

}
