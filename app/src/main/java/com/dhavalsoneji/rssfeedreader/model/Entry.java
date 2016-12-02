package com.dhavalsoneji.rssfeedreader.model;

public class Entry {
    private String id;
    private String title;
    private String content;
    private String published;
    private String postedBY;

    public Entry(String id, String title, String content, String published, String postedBy) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.published = published;
        this.postedBY = postedBy;
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

    public String getPostedBY() {
        return postedBY;
    }

    public void setPostedBY(String postedBY) {
        this.postedBY = postedBY;
    }
}
