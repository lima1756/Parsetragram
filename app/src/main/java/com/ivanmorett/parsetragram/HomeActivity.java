package com.ivanmorett.parsetragram;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.ivanmorett.parsetragram.Models.Post;
import com.ivanmorett.parsetragram.fragments.CameraFragment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        // do something here
                        return true;
                    case R.id.action_new:
                        // Replace the contents of the container with the new fragment
                        ft.replace(R.id.fragmentContainer, new CameraFragment());
                        // or ft.add(R.id.your_placeholder, new FooFragment());
                        // Complete the changes added above
                        ft.commit();
                        return true;
                    case R.id.action_user:
                        // do something here
                        return true;
                }
                return false;
            }
        });

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
