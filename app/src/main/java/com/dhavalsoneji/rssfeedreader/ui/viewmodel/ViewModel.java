package com.dhavalsoneji.rssfeedreader.ui.viewmodel;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.dhavalsoneji.rssfeedreader.R;
import com.dhavalsoneji.rssfeedreader.model.Entry;
import com.dhavalsoneji.rssfeedreader.ui.activity.MainActivity;
import com.dhavalsoneji.rssfeedreader.utils.Applog;
import com.dhavalsoneji.rssfeedreader.utils.FeedReader;
import com.dhavalsoneji.rssfeedreader.utils.Utils;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ViewModel {
    public static final String TAG = ViewModel.class.getSimpleName();
    private MainActivity mActivity;
    private ProgressDialog mDialog;

    public ViewModel(MainActivity activity) {
        mActivity = activity;
    }

    public void initRequest(String url) {
        if (Utils.checkInternetConnection(mActivity)) {

            try {

                final URL location = new URL(url);
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
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected List<Entry> doInBackground(Void... voids) {
            List<Entry> list = new ArrayList<>();
            try {
                InputStream stream = Utils.downloadUrl(mLocation);

                FeedReader feedReader = new FeedReader();
                list = feedReader.parseBloggerFeed(stream);

            } catch (Exception e) {
                Applog.e(TAG, e.getMessage(), e);
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<Entry> entries) {
            super.onPostExecute(entries);
            hideProgressDialog();
            try {
                if (Utils.isValidList(entries)) {
                    mActivity.displayEntry(entries);
                } else {
                    Utils.showToast(mActivity, mActivity.getResources().getString(R.string.empty_list_found));
                }
            } catch (Exception e) {
                Applog.e(TAG, e.getMessage(), e);
            }
        }
    }

    public void showProgressDialog() {
        try {
            mDialog = null;
            mDialog = new ProgressDialog(mActivity);
            mDialog.setMessage(mActivity.getResources().getString(R.string.please_wait));
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
