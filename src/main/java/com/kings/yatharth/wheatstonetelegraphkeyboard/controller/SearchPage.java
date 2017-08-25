package com.kings.yatharth.wheatstonetelegraphkeyboard.controller;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.kings.yatharth.wheatstonetelegraphkeyboard.R;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineRecyclerViewAdapter;

public class SearchPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        InputMethodManager im = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        String list = im.getEnabledInputMethodList().toString();
        if(!list.contains("com.kings.yatharth.wheatstonetelegraphkeyboard/.controller.MyKeyboard")){
            Log.i("List", list);
            Toast.makeText(getApplicationContext(),"Please enable the Wheatstone Keyboard!.", Toast.LENGTH_LONG).show();
            this.startActivity(new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS));
            finish();
        }


        final EditText query = (EditText) findViewById(R.id.search_tweet);
        ImageButton search = (ImageButton) findViewById(R.id.search_btn);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.search_results);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchTimeline searchTimeline = new SearchTimeline.Builder()
                        .query("#" + query.getText().toString())
                        .maxItemsPerRequest(100)
                        .build();

                final TweetTimelineRecyclerViewAdapter adapter =
                        new TweetTimelineRecyclerViewAdapter.Builder(SearchPage.this)
                                .setTimeline(searchTimeline)
                                .setViewStyle(R.style.tw__TweetLightWithActionsStyle)
                                .build();

                recyclerView.setAdapter(adapter);
            }
        });

        Button tut = (Button) findViewById(R.id.tut_btn);
        tut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SearchPage.this, Tutorial.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String currentKeyboard = Settings.Secure.getString(getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);

        if (!currentKeyboard.equals("com.kings.yatharth.wheatstonetelegraphkeyboard/.controller.MyKeyboard")) {
            Toast.makeText(getApplicationContext(), "Please select the Wheatstone Telegraph. ", Toast.LENGTH_LONG).show();
            Log.i("Name", currentKeyboard);
            InputMethodManager imeManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
            imeManager.showInputMethodPicker();
        }


    }
}
