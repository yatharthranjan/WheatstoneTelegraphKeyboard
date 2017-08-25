package com.kings.yatharth.wheatstonetelegraphkeyboard.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

import com.kings.yatharth.wheatstonetelegraphkeyboard.R;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.FixedTweetTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineRecyclerViewAdapter;
import java.util.List;
import retrofit2.Call;

public class HomeTimeline extends AppCompatActivity {

    FixedTweetTimeline fixedTweetTimeline;
    TwitterApiClient twitterApiClient;
    StatusesService statusesService;
    RecyclerView recyclerView;
    TweetTimelineRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_timeline);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        twitterApiClient = TwitterCore.getInstance().getApiClient();
        statusesService = twitterApiClient.getStatusesService();



        Call<List<Tweet>> tweets =  statusesService.homeTimeline(100,null,null,false,false,true,true);


        tweets.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                List<Tweet> tweetList = result.data;
                fixedTweetTimeline = new FixedTweetTimeline.Builder()
                        .setTweets(tweetList)
                        .build();

                if(fixedTweetTimeline != null) {
                    adapter = new TweetTimelineRecyclerViewAdapter.Builder(HomeTimeline.this)
                                    .setTimeline(fixedTweetTimeline)
                                    .setViewStyle(R.style.tw__TweetLightWithActionsStyle)
                                    .build();

                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(getApplicationContext(),"Cannot retrieve tweets. "+ exception.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        Call<List<Tweet>> tweets =  statusesService.homeTimeline(100,null,null,false,false,true,true);


        tweets.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                List<Tweet> tweetList = result.data;
                fixedTweetTimeline = new FixedTweetTimeline.Builder()
                        .setTweets(tweetList)
                        .build();

                if(fixedTweetTimeline != null) {
                    adapter = new TweetTimelineRecyclerViewAdapter.Builder(HomeTimeline.this)
                                    .setTimeline(fixedTweetTimeline)
                                    .setViewStyle(R.style.tw__TweetLightWithActionsStyle)
                                    .build();
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(getApplicationContext(),"Cannot retrieve tweets. "+ exception.getMessage(),Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_search:
                Intent i =new Intent(HomeTimeline.this, SearchPage.class);
                startActivity(i);
                return true;

            case R.id.action_add:
                Intent in =new Intent(HomeTimeline.this, ComposeTweet.class);
                startActivity(in);
                return true;

            case R.id.action_refresh:
                onResume();
                return true;

            case R.id.action_logout:
                CookieSyncManager.createInstance(this);
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.removeSessionCookie();
                TwitterCore.getInstance().getSessionManager().clearActiveSession();
                Intent intent = new Intent(HomeTimeline.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;

            case R.id.action_tutorial:
                in = new Intent(HomeTimeline.this, Tutorial.class);
                startActivity(in);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
