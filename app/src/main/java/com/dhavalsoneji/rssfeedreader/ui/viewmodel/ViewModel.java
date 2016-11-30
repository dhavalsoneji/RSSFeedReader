package com.dhavalsoneji.rssfeedreader.ui.viewmodel;

import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.dhavalsoneji.rssfeedreader.R;
import com.dhavalsoneji.rssfeedreader.model.Entry;
import com.dhavalsoneji.rssfeedreader.ui.activity.MainActivity;
import com.dhavalsoneji.rssfeedreader.utils.AppConstants;
import com.dhavalsoneji.rssfeedreader.utils.Applog;
import com.dhavalsoneji.rssfeedreader.utils.Utils;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dhaval Soneji on 30/11/16.
 */
public class ViewModel {
    public static final String TAG = ViewModel.class.getSimpleName();
    private MainActivity mActivity;

    public ViewModel(MainActivity activity) {
        mActivity = activity;
    }

    public void initToolbar() {
        Toolbar toolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        mActivity.setSupportActionBar(toolbar);
    }

    public void initFab() {
        FloatingActionButton fab = (FloatingActionButton) mActivity.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void initRequest() {
        if (Utils.checkInternetConnection(mActivity)) {

            try {

                final URL location = new URL(AppConstants.URL_BLOG_FEED);
                new FeedReaderAsyncTask(location).execute();

            } catch (Exception e) {
                Applog.e(TAG, e.getMessage(), e);
            }

        } else {
            Utils.showToast(mActivity, mActivity.getResources().getString(R.string.no_internet));
        }
    }

    private class FeedReaderAsyncTask extends AsyncTask<Void, Void, List<Entry>> {

        private URL mLocation;

        public FeedReaderAsyncTask(URL location) {
            mLocation = location;
        }

        @Override
        protected List<Entry> doInBackground(Void... voids) {
            List<Entry> list = new ArrayList<>();
            try {
                InputStream stream = Utils.downloadUrl(mLocation);


            } catch (Exception e) {
                Applog.e(TAG, e.getMessage(), e);
            }
            return list;
        }
    }
}
