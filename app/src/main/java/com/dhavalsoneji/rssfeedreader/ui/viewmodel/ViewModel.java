package com.dhavalsoneji.rssfeedreader.ui.viewmodel;

import com.dhavalsoneji.rssfeedreader.model.Entry;
import com.dhavalsoneji.rssfeedreader.ui.activity.MainActivity;
import com.dhavalsoneji.rssfeedreader.ui.listener.LoadCompleteListener;
import com.dhavalsoneji.rssfeedreader.utils.RSSFeedReader;

import java.util.List;

public class ViewModel {
    public static final String TAG = ViewModel.class.getSimpleName();
    private MainActivity mActivity;
    private RSSFeedReader feedReader;

    public ViewModel(MainActivity activity) {
        mActivity = activity;
        feedReader = new RSSFeedReader();
    }

    public void initRequest(String url) {
        feedReader.execute(mActivity, url, new LoadCompleteListener() {
            @Override
            public void display(List<Entry> list) {
                mActivity.displayEntry(list);
            }
        });
    }
}
