package com.dhavalsoneji.rssfeedreader.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.dhavalsoneji.rssfeedreader.R;
import com.dhavalsoneji.rssfeedreader.model.Entry;
import com.dhavalsoneji.rssfeedreader.ui.adapter.ListAdapter;
import com.dhavalsoneji.rssfeedreader.ui.viewmodel.ViewModel;
import com.dhavalsoneji.rssfeedreader.utils.AppConstants;
import com.dhavalsoneji.rssfeedreader.utils.Applog;
import com.google.gson.Gson;

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

    public void displayEntry(List<Entry> list) {
        Applog.i(TAG, new Gson().toJson(list));
        mListAdapter.updateList(list);
    }
}
