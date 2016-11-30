package com.dhavalsoneji.rssfeedreader.custom;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.dhavalsoneji.rssfeedreader.R;
import com.dhavalsoneji.rssfeedreader.model.Entry;
import com.dhavalsoneji.rssfeedreader.ui.adapter.ListAdapter;
import com.dhavalsoneji.rssfeedreader.utils.AppConstants;
import com.dhavalsoneji.rssfeedreader.utils.Applog;
import com.dhavalsoneji.rssfeedreader.utils.FeedReader;
import com.dhavalsoneji.rssfeedreader.utils.Utils;
import com.google.gson.Gson;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BRssReader extends LinearLayout {
    private static final String TAG = BRssReader.class.getSimpleName();
    private AttributeSet mAttrs;
    private Context mContext;
    private String mBolggerUrl;

    private ListView mListView;
    //    private ViewModel mViewModel;
    private ListAdapter mListAdapter;
    private ProgressDialog mDialog;

    public BRssReader(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.activity_brss_reader, this, false);
        mContext = context;
        initialCall();
    }

    public BRssReader(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.activity_brss_reader, this, false);
        mContext = context;
        mAttrs = attrs;
        initialCall();
    }

    public BRssReader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.activity_brss_reader, this, false);
        mContext = context;
        mAttrs = attrs;
        initialCall();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BRssReader(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LayoutInflater.from(context).inflate(R.layout.activity_brss_reader, this, false);
        mContext = context;
        mAttrs = attrs;
        initialCall();
    }

    private void initialCall() {

//        mViewModel = new ViewModel(this);
        mListView = (ListView) findViewById(R.id.listViewPost);
        List<Entry> list = new ArrayList<>();
        mBolggerUrl = AppConstants.URL_BLOG_FEED;

        if (mAttrs != null) {
            TypedArray typedArray = mContext.getTheme().obtainStyledAttributes(mAttrs,
                    R.styleable.BRssReader, 0, 0);

            try {
                String bloggerUrl = typedArray.getString(R.styleable.BRssReader_blogger_url);
                validateAttr(bloggerUrl);
            } finally {
                typedArray.recycle();
            }
        } else {
            initRequest(mBolggerUrl);
        }
    }

    private void validateAttr(String bloggerUrl) {
        if (Utils.validateUrl(bloggerUrl)) {
            mBolggerUrl = bloggerUrl;
        } else {
            mBolggerUrl = AppConstants.URL_BLOG_FEED;
        }

        initRequest(mBolggerUrl);
    }

    public void displayEntry(List<Entry> list) {
        Applog.e(TAG, new Gson().toJson(list));
        mListAdapter = new ListAdapter(mContext, list);
//        mListAdapter.updateList(list);
        mListView.setAdapter(mListAdapter);
    }

    public void changeUrl(String bloggerUrl) {
        if (Utils.validateUrl(bloggerUrl)) {
            initRequest(bloggerUrl);
        } else {
            Utils.showToast(mContext, mContext.getResources().getString(R.string.invalid_url));
        }
    }

    public void initRequest(String url) {
        if (Utils.checkInternetConnection(mContext)) {

            try {

                final URL location = new URL(url);
                new FeedReaderAsyncTask(location).execute();

            } catch (Exception e) {
                Applog.e(TAG, e.getMessage(), e);
            }

        } else {
            Utils.showToast(mContext, mContext.getResources().getString(R.string.no_internet));
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
//            showProgressDialog();
        }

        @Override
        protected List<Entry> doInBackground(Void... voids) {
            List<Entry> list = new ArrayList<>();
            try {
                InputStream stream = Utils.downloadUrl(mLocation);

                FeedReader feedReader = new FeedReader();
                list = feedReader.parse(stream);

            } catch (Exception e) {
                Applog.e(TAG, e.getMessage(), e);
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<Entry> entries) {
            super.onPostExecute(entries);
//            hideProgressDialog();
            try {
                if (Utils.isValidList(entries)) {
                    displayEntry(entries);
                } else {
                    Utils.showToast(mContext, mContext.getResources().getString(R.string.empty_list_found));
                }
            } catch (Exception e) {
                Applog.e(TAG, e.getMessage(), e);
            }
        }
    }

    public void showProgressDialog() {
        try {
            mDialog = null;
            mDialog = new ProgressDialog(mContext);
            mDialog.setMessage(mContext.getResources().getString(R.string.please_wait));
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
