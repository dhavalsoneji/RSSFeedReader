package com.dhavalsoneji.rssfeedreader.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Xml;

import com.dhavalsoneji.rssfeedreader.model.Entry;
import com.dhavalsoneji.rssfeedreader.ui.listener.LoadCompleteListener;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class RSSFeedReader {
    private static final String TAG = RSSFeedReader.class.getSimpleName();

    // We don't use XML namespaces
    private static final String ns = null;
    private static final String NO_INTERNET = "No internet connection found.\nCheck your connection.";
    private static final String EMPTY_LIST_FOUND = "Empty list found!";
    private static final String LOADING = "Loading...";
    private static final String INVALID_URL = "Invalid url / Not a Blogspot blog's url";
    private static final String PARSING_FAILED = "Parsing Exception, please cross check url sends xml data?";
    private ProgressDialog mDialog;

    public void execute(Context context, String url, LoadCompleteListener loadCompleteListener) {
        if (Utils.checkInternetConnection(context)) {
            if (Utils.isValidBlogspotUrl(url)) {
                try {
                    final URL location = new URL(url);
                    new FeedReaderAsyncTask(location, context, loadCompleteListener).execute();

                } catch (Exception e) {
                    Applog.e(TAG, e.getMessage(), e);
                }
            } else {
                Utils.showToast(context, INVALID_URL);
            }

        } else {
            Utils.showToast(context, NO_INTERNET);
        }
    }

    private class FeedReaderAsyncTask extends AsyncTask<Void, Void, List<Entry>> {

        private LoadCompleteListener mLoadCompleteListener;
        private Context mContext;
        private URL mLocation;

        FeedReaderAsyncTask(URL location, Context context, LoadCompleteListener loadCompleteListener) {
            mLocation = location;
            mContext = context;
            mLoadCompleteListener = loadCompleteListener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog(mContext);
        }

        @Override
        protected List<Entry> doInBackground(Void... voids) {
            List<Entry> list = new ArrayList<>();
            try {
                InputStream stream = Utils.downloadUrl(mLocation);

                RSSFeedReader feedReader = new RSSFeedReader();
                list = feedReader.parseBloggerFeed(stream);

            } catch (Exception e) {
                Utils.showToast(mContext, PARSING_FAILED);
                Applog.e(TAG, e.getMessage(), e);
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<Entry> entries) {
            super.onPostExecute(entries);
            hideProgressDialog(mContext);
            try {
                if (Utils.isValidList(entries)) {
                    mLoadCompleteListener.display(entries);
                } else {
                    Utils.showToast(mContext, EMPTY_LIST_FOUND);
                }
            } catch (Exception e) {
                Applog.e(TAG, e.getMessage(), e);
            }
        }
    }

    private List<Entry> parseBloggerFeed(InputStream in)
            throws XmlPullParserException, IOException, ParseException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readBloggerFeed(parser);
        } catch (Exception e) {
            Applog.e(TAG, e.getMessage(), e);
        } finally {
            in.close();
        }
        return null;
    }

    private List<Entry> readBloggerFeed(XmlPullParser parser)
            throws XmlPullParserException, IOException, ParseException {
        List<Entry> entries = new ArrayList<>();

        // Search for <feed> tags. These wrap the beginning/end of an Atom document.
        //
        // Example:
        // <feed>
        // ...
        // </feed>
        try {
            parser.require(XmlPullParser.START_TAG, ns, "feed");
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }

//              Starts by looking for the <entry> tag. This tag repeates inside of <feed> for each
//              article in the feed.
//              Example:
//                  <entry>
//                      <id>tag:blogger.com,1999:blog-3380459536257842470.post-6169024778464572293</id>
//                      <published>2016-11-29T23:20:00.001-08:00</published>
//                      <title type='text'>Post #3</title>
//                      <content>Content of post</content>
//                      ...
//                  <entry>
                if (parser.getName().equals("entry")) {
                    entries.add(readEntry(parser));
                } else {
                    skip(parser);
                }
            }
        } catch (Exception e) {
            Applog.e(TAG, e.getMessage(), e);
        }
        return entries;
    }

    /**
     * Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them
     * off to their respective "read" methods for processing. Otherwise, skips the tag.
     */
    private Entry readEntry(XmlPullParser parser)
            throws XmlPullParserException, IOException, ParseException {
        parser.require(XmlPullParser.START_TAG, ns, "entry");
        String title = null;
        String published = null;
        String content = null;
        String postedBy = null;
        String id = null;

        try {
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                if (name.equals("id")) {
                    // Example: <id>Article id</id>
                    id = readBasicTag(parser, "id");
                } else if (name.equals("title")) {
                    // Example: <title>Article title</title>
                    title = readBasicTag(parser, "title");
                } else if (name.equals("published")) {
                    // Example: <published>Article published date</published>
                    published = readBasicTag(parser, "published");
                } else if (name.equals("content")) {
                    // Example <content>Article content</content>
                    content = readBasicTag(parser, "content");
                } else if (name.equals("author")) {

                    postedBy = "";
                    parser.require(XmlPullParser.START_TAG, ns, "author");
                    while (parser.next() != XmlPullParser.END_TAG) {
                        if (parser.getEventType() != XmlPullParser.START_TAG) {
                            continue;
                        }

                        if (parser.getName().equals("name")) {
                            postedBy = "Posted by " + readBasicTag(parser, "name");
                        } else {
                            skip(parser);
                        }
                    }
                }

                /*else if (name.equals("published")) {
                    // Example: <published>2003-06-27T12:00:00Z</published>
                    Time t = new Time();
                    t.parse3339(readTag(parser, TAG_PUBLISHED));
                    publishedOn = t.toMillis(false);
                }*/
                else {
                    skip(parser);
                }
            }
        } catch (Exception e) {
            Applog.e(TAG, e.getMessage(), e);
        }
        return new Entry(id, title, content, published, postedBy);
    }

    /**
     * Reads the body of a basic XML tag, which is guaranteed not to contain any nested elements.
     * <p>
     * <p>You probably want to call readTag().
     *
     * @param parser Current parser object
     * @param tag    XML element tag name to parseBloggerFeed
     * @return Body of the specified tag
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readBasicTag(XmlPullParser parser, String tag)
            throws IOException, XmlPullParserException {
        try {
            parser.require(XmlPullParser.START_TAG, ns, tag);
            String result = readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, tag);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Processes link tags in the feed.
     */
    private String readAlternateLink(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        String link = null;
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String tag = parser.getName();
        String relType = parser.getAttributeValue(null, "rel");
        if (relType.equals("alternate")) {
            link = parser.getAttributeValue(null, "href");
        }
        while (true) {
            if (parser.nextTag() == XmlPullParser.END_TAG) break;
            // Intentionally break; consumes any remaining sub-tags.
        }
        return link;
    }

    /**
     * For the tags title and summary, extracts their text values.
     */
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = null;
        try {
            if (parser.next() == XmlPullParser.TEXT) {
                result = parser.getText();
                parser.nextTag();
            }
        } catch (Exception e) {
            Applog.e(TAG, e.getMessage(), e);
        }
        return result;
    }

    /**
     * Skips tags the parser isn't interested in. Uses depth to handle nested tags. i.e.,
     * if the next tag after a START_TAG isn't a matching END_TAG, it keeps going until it
     * finds the matching END_TAG (as indicated by the value of "depth" being 0).
     */
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        try {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                throw new IllegalStateException();
            }
            int depth = 1;
            while (depth != 0) {
                switch (parser.next()) {
                    case XmlPullParser.END_TAG:
                        depth--;
                        break;
                    case XmlPullParser.START_TAG:
                        depth++;
                        break;
                }
            }
        } catch (Exception e) {
            Applog.e(TAG, e.getMessage(), e);
        }
    }

    private void showProgressDialog(Context mContext) {
        try {
            mDialog = null;
            mDialog = new ProgressDialog(mContext);
            mDialog.setMessage(LOADING);
            mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mDialog.setCanceledOnTouchOutside(true);
            mDialog.setCancelable(true);
            mDialog.show();
        } catch (Exception e) {
            Applog.e(TAG, e.getMessage(), e);
        }
    }

    private void hideProgressDialog(Context mContext) {
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
