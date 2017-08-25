package com.kings.yatharth.wheatstonetelegraphkeyboard.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kings.yatharth.wheatstonetelegraphkeyboard.R;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class MainActivity extends AppCompatActivity {

    TwitterLoginButton loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TwitterConfig config = new TwitterConfig.Builder(getApplicationContext())
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig("lXsZ5NM6xy2CYRSFB96ieIEt9", "P14ZnKStPwoLA6yAd1R4zB7KvNxmedeenC7TX1Nk5Jru0amhtA"))
                .debug(true)
                .build();
        Twitter.initialize(config);

        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        if(session != null){
            Intent in =new Intent(MainActivity.this, HomeTimeline.class);
            startActivity(in);
            finish();
        }


        loginButton = (TwitterLoginButton) findViewById(R.id.login_button);
        loginButton.setEnabled(true);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                Toast.makeText(getApplicationContext(),"Login Successful !",Toast.LENGTH_LONG).show();

                Intent i = new Intent(MainActivity.this, HomeTimeline.class);
                startActivity(i);
                finish();
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                Toast.makeText(getApplicationContext(),"Login Failed! Please Try Again." + exception.getMessage(),Toast.LENGTH_LONG).show();
            }
        });


        Button tryButton = (Button) findViewById(R.id.try_btn);
        tryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, SearchPage.class);
                startActivity(in);

            }
        });

        Button info = (Button) findViewById(R.id.info_charles);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, OpenLink.class);
                startActivity(in);
            }
        });

        Button tut= (Button) findViewById(R.id.tut_btn);
        tut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, Tutorial.class);
                startActivity(in);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }
}
