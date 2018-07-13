package com.ivanmorett.parsetragram.Models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ParseClassName("Post")
public class Post extends ParseObject {
    private final static String KEY_DESCRIPTION = "description";
    private final static String KEY_IMAGE = "image";
    private final static String KEY_USER = "user";

    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage(){
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image){
        put(KEY_IMAGE, image);
    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    public int getLikesCount() {
        List<String> list = getList("likedBy");
        return list!=null?list.size():0;
    }

    public boolean isLiked() {
        List<String> list = getList("likedBy");
        return list!=null && list.contains(ParseUser.getCurrentUser().getObjectId());
    }

    public void addLike(String id) {
        List<String> list = getList("likedBy");
        if(list==null)
            list = new ArrayList<>();
        list.add(id);
        setLikedBy(list);
    }

    public void removeLike(String id){
        List<String> list = getList("likedBy");
        if(list==null)
            list = new ArrayList<>();
        list.remove(id);
        setLikedBy(list);
    }

    private void setLikedBy(List<String> list){
        put("likedBy", list);
    }

    public static class Query extends ParseQuery<Post>{
        public Query(){
            super(Post.class);
        }
        private int limit;

        public Query orderByDescending(String string){
            super.orderByDescending("createdAt");
            return this;
        }

        public Query withUser(){
            include(KEY_USER);
            return this;
        }

        public Query setLimit(int limit){
            this.limit = limit>0?limit:20;
            super.setLimit(limit);
            return this;
        }

        public Query getPage(int page){
            setSkip(limit * page);
            return this;
        }
    }
}
