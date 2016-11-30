package com.dhavalsoneji.rssfeedreader.utils;

import android.util.Xml;

import com.dhavalsoneji.rssfeedreader.model.Entry;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dhaval Soneji on 30/11/16.
 */

public class BFeedReader {
    private static final String TAG = BFeedReader.class.getSimpleName();
    // Constants indicting XML element names that we're interested in
    private static final int TAG_TITLE = 1;
    private static final int TAG_LINK = 2;
    private static final int TAG_PUBDATE = 3;
    private static final int TAG_CREATOR = 4;
    private static final int TAG_CATEGORY = 5;
    private static final int TAG_GUID = 6;
    private static final int TAG_DESCRIPTION = 7;
    private static final int TAG_POSIT = 8;

    // We don't use XML namespaces
    private static final String ns = null;

    public List<Entry> parse(InputStream in)
            throws XmlPullParserException, IOException, ParseException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } catch (Exception e) {
            Applog.e(TAG, e.getMessage(), e);
        } finally {
            in.close();
        }
        return null;
    }

    private List<Entry> readFeed(XmlPullParser parser)
            throws XmlPullParserException, IOException, ParseException {
        List<Entry> entries = new ArrayList<>();

        // Search for <rss> tags. These wrap the beginning/end of an Atom document.
        //
        // Example:
        // <?xml version="1.0" encoding="utf-8"?>
        // <rss xmlns="http://www.w3.org/2005/Atom">
        // ...
        // </rss>
        try {
            parser.require(XmlPullParser.START_TAG, ns, "rss");
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }

                if (parser.getName().equals("channel")) {
                    parser.require(XmlPullParser.START_TAG, ns, "channel");
                    while (parser.next() != XmlPullParser.END_TAG) {
                        if (parser.getEventType() != XmlPullParser.START_TAG) {
                            continue;
                        }


                        // Starts by looking for the <item> tag. This tag repeates inside of <channel> for each
                        // article in the feed.
                        //
                        // Example:
                        // <item>
                        //   <title>Article title</title>
                        //   <link rel="alternate" type="text/html" href="http://example.com/article/1234"/>
                        //   <link rel="edit" href="http://example.com/admin/article/1234"/>
                        //   <id>urn:uuid:218AC159-7F68-4CC6-873F-22AE6017390D</id>
                        //   <published>2003-06-27T12:00:00Z</published>
                        //   <updated>2003-06-28T12:00:00Z</updated>
                        //   <summary>Article summary goes here.</summary>
                        //   <author>
                        //     <name>Rick Deckard</name>
                        //     <email>deckard@example.com</email>
                        //   </author>
                        // </item>
                        if (parser.getName().equals("item")) {
                            entries.add(readEvent(parser));
                        } else {
                            skip(parser);
                        }
                    }
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
    private Entry readEvent(XmlPullParser parser)
            throws XmlPullParserException, IOException, ParseException {
        parser.require(XmlPullParser.START_TAG, ns, "item");
        String title = null;
        String link = null;
        String pubdate = null;
        String creator = null;
        String category = null;
        String guid = null;
        String description = null;
        String postId = null;
        long publishedOn = 0;

        try {
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                if (name.equals("post-id")) {
                    // Example: <id>urn:uuid:218AC159-7F68-4CC6-873F-22AE6017390D</id>
                    postId = readTag(parser, TAG_POSIT);
                } else if (name.equals("title")) {
                    // Example: <title>Article title</title>
                    title = readTag(parser, TAG_TITLE);
                } else if (name.equals("link")) {
                    // Example: <link rel="alternate" type="text/html" href="http://example.com/article/1234"/>
                    //
                    // Multiple link types can be included. readAlternateLink() will only return
                    // non-null when reading an "alternate"-type link. Ignore other responses.
                    String tempLink = readTag(parser, TAG_LINK);
                    if (tempLink != null) {
                        link = tempLink;
                    }
                } else if (name.equals("pubDate")) {
                    pubdate = readTag(parser, TAG_PUBDATE);
                } else if (name.equals("description")) {
                    description = readTag(parser, TAG_DESCRIPTION);
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
        return new Entry(postId, title, description, pubdate);
    }

    /**
     * Process an incoming tag and read the selected value from it.
     */
    private String readTag(XmlPullParser parser, int tagType)
            throws IOException, XmlPullParserException {
        String tag = null;
        String endTag = null;

        try {
            switch (tagType) {
                case TAG_POSIT:
                    return readBasicTag(parser, "post-id");
                case TAG_TITLE:
                    return readBasicTag(parser, "title");
                case TAG_DESCRIPTION:
                    return readBasicTag(parser, "description");
                case TAG_LINK:
                    return readBasicTag(parser, "link");
                case TAG_PUBDATE:
                    return readBasicTag(parser, "pubDate");
                //                return readAlternateLink(parser);
                default:
                    throw new IllegalArgumentException("Unknown tag type: " + tagType);
            }
        } catch (Exception e) {
            Applog.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    /**
     * Reads the body of a basic XML tag, which is guaranteed not to contain any nested elements.
     * <p>
     * <p>You probably want to call readTag().
     *
     * @param parser Current parser object
     * @param tag    XML element tag name to parse
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

    /**
     * This class represents a single entry (post) in the XML feed.
     * <p>
     * <p>It includes the data members "title," "link,","postId", and "description."
     */
}
