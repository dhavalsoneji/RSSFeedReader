package com.dhavalsoneji.rssfeedreader.ui.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.dhavalsoneji.rssfeedreader.R;
import com.dhavalsoneji.rssfeedreader.model.Entry;
import com.dhavalsoneji.rssfeedreader.ui.adapter.ListAdapter;
import com.dhavalsoneji.rssfeedreader.ui.viewmodel.ViewModel;
import com.dhavalsoneji.rssfeedreader.utils.AppConstants;
import com.dhavalsoneji.rssfeedreader.utils.Applog;
import com.dhavalsoneji.rssfeedreader.utils.FeedReader;
import com.dhavalsoneji.rssfeedreader.utils.Utils;
import com.google.gson.Gson;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ListView mListView;
    private ListAdapter mListAdapter;
    private ProgressDialog mDialog;
    private ViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.listViewPost);
        List<Entry> list = new ArrayList<>();
        mListAdapter = new ListAdapter(this, list);
        mListView.setAdapter(mListAdapter);

        mViewModel = new ViewModel(this);
        mViewModel.initRequest(AppConstants.URL_BLOG_FEED);
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

    public void displayEntry(List<Entry> list) {
        Applog.e(TAG, new Gson().toJson(list));
        mListAdapter.updateList(list);
    }

    public void showProgressDialog() {
        try {
            mDialog = null;
            mDialog = new ProgressDialog(this);
            mDialog.setMessage(getResources().getString(R.string.please_wait));
            mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mDialog.setCanceledOnTouchOutside(true);
            mDialog.setCancelable(true);
            mDialog.show();
        } catch (Exception e) {
            Applog.e(TAG, e.getMessage(), e);
        }
    }

    public void hideProgressDialog() {
        try {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
                mDialog = null;
            }
        } catch (Exception e) {
            Applog.e(TAG, e.getMessage(), e);
        }
    }
}
