package com.dhavalsoneji.rssfeedreader.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.dhavalsoneji.rssfeedreader.R;
import com.dhavalsoneji.rssfeedreader.custom.BRssReader;
import com.dhavalsoneji.rssfeedreader.utils.AppConstants;

public class MainActivity extends AppCompatActivity {
    private BRssReader rssReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rssReader = (BRssReader) findViewById(R.id.rssReader);
        rssReader.changeUrl(AppConstants.URL_BLOG_FEED);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
