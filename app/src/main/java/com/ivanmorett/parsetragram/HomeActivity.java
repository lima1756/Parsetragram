package com.ivanmorett.parsetragram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ivanmorett.parsetragram.Models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser();
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e==null){
                    for(int i = 0; i<objects.size(); i++){
                        Log.d("HomeActivity", String.format("Object %s: %s\n user:%s", i,objects.get(i).getDescription(), objects.get(i).getUser().getUsername()));
                    }
                }
                else{
                    e.printStackTrace();
                }
            }
        });
    }
}
