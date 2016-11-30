package com.dhavalsoneji.rssfeedreader.model;

/**
 * Created by Dhaval Soneji Riontech on 30/11/16.
 */
public class Entry {
    private String id;
    private String title;
    private String content;
    private String published;

    public Entry(String id, String title, String content, String published) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.published = published;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }
}
