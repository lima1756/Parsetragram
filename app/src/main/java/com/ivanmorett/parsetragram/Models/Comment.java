package com.ivanmorett.parsetragram.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("Comment")
public class Comment extends ParseObject {
    private final static String KEY_COMMENT = "comment";
    private final static String KEY_USER = "user";
    private final static String KEY_POST = "post";

    public String getComment(){
        return getString(KEY_COMMENT);
    }

    public void setComment(String description){
        put(KEY_COMMENT, description);
    }


    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    public Post getPost() { return (Post) get(KEY_POST); }

    public void setPost(Post post) { put(KEY_POST, post); }


    public static class Query extends ParseQuery<Comment> {
        public Query(){
            super(Comment.class);
        }
        private int limit;

        public Comment.Query orderByDescending(String string){
            super.orderByDescending("createdAt");
            return this;
        }

        public Comment.Query contains(String key, String value){
            whereContains(key, value);
            return this;
        }

        public Comment.Query withUser(){
            include(KEY_USER);
            return this;
        }

        public Comment.Query setLimit(int limit){
            this.limit = limit>0?limit:20;
            super.setLimit(limit);
            return this;
        }

        public Comment.Query getPage(int page){
            setSkip(limit * page);
            return this;
        }
    }
}
