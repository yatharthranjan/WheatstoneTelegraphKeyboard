package com.kings.yatharth.wheatstonetelegraphkeyboard.controller;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kings.yatharth.wheatstonetelegraphkeyboard.R;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComposeTweet extends AppCompatActivity {

    ImageView iv;
    private static final String IMAGE_TYPES = "image/*";
    private static final int IMAGE_PICKER_CODE = 141;
    String[] hashs = {};
    int f=0;
    Uri uri;
    String text;
    int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 12341;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_tweet);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        iv = (ImageView) findViewById(R.id.image_view);
        iv.setVisibility(View.GONE);


        InputMethodManager im = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        String list = im.getEnabledInputMethodList().toString();
        if(!list.contains("com.kings.yatharth.wheatstonetelegraphkeyboard/.controller.MyKeyboard")){
            Log.i("List", list);
            Toast.makeText(getApplicationContext(),"Please enable the Wheatstone Keyboard!.", Toast.LENGTH_LONG).show();
            this.startActivity(new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS));
            finish();

        }

        if(android.os.Build.VERSION.SDK_INT >= 23)
        getPermissions();


        final EditText typePost = (EditText) findViewById(R.id.compose_tweet);
        Button post = (Button) findViewById(R.id.post_tweet);


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    ArrayList<String> hashtags = new ArrayList<String>();

                    text = typePost.getText().toString();
                    String regexPattern = "(#\\w+)";

                    Pattern p = Pattern.compile(regexPattern);
                    Matcher m = p.matcher(text);
                    while (m.find()) {
                        String hashtag = m.group(1);
                        // Add hashtag to ArrayList
                        hashtags.add(hashtag);
                    }


                    hashtags.toArray(hashs);
                    launchComposer(uri);

                } catch (Exception e) {
                    Log.e("Error", "error creating tweet intent", e);
                }
            }
        });
    }

    @TargetApi(23)
    public void getPermissions(){

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
            // app-defined int constant that should be quite unique
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.compose_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_image:
                launchPicker();
                return true;

            case R.id.action_tutorial:
                Intent in = new Intent(ComposeTweet.this, Tutorial.class);
                startActivity(in);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    void launchPicker() {
        final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(IMAGE_TYPES);
        startActivityForResult(Intent.createChooser(intent, "Pick an Image"), IMAGE_PICKER_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICKER_CODE && resultCode == Activity.RESULT_OK) {
            uri = data.getData();
            iv.setVisibility(View.VISIBLE);
            iv.setImageURI(uri);
        }
    }

    void launchComposer(Uri uri) {
        final TwitterSession session = TwitterCore.getInstance().getSessionManager()
                .getActiveSession();
        final Intent intent = new ComposerActivity.Builder(ComposeTweet.this)
                .session(session)
                .image(uri)
                .text(text)
                .hashtags(hashs)
                .createIntent();
        startActivity(intent);
        finish();
    }

}

