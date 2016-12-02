# RSSFeedReader

Example:

Blogger Blog url: http://github-rssfeedreader.blogspot.com

Blogger Blog RSS feed url: http://github-rssfeedreader.blogspot.com/feeds/posts/default (used this)

To Use:

private RSSFeedReader feedReader;

feedReader.execute(mActivity, url, new LoadCompleteListener() {
            @Override
            public void display(List<Entry> list) {
                mActivity.displayEntry(list);
            }
        });
