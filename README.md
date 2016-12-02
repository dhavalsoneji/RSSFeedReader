# RSSFeedReader

Example:

Blogger Blog url: http://github-rssfeedreader.blogspot.com

Blogger Blog RSS feed url: http://github-rssfeedreader.blogspot.com/feeds/posts/default (used this)

To Use:

```java
private RSSFeedReader feedReader;
private String url = "http://github-rssfeedreader.blogspot.com/feeds/posts/default";

feedReader.execute(context, url, new LoadCompleteListener() {
            @Override
            public void display(List<Entry> list) {
                mActivity.displayEntry(list);
            }
        });
</code>
```
