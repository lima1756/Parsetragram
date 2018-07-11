package com.ivanmorett.parsetragram;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.ivanmorett.parsetragram.Models.Post;
import com.ivanmorett.parsetragram.fragments.CameraFragment;
import com.ivanmorett.parsetragram.fragments.FeedFragment;
import com.ivanmorett.parsetragram.fragments.UserFragment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.bottom_navigation) BottomNavigationView bnBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        final FeedFragment feedFragment = new FeedFragment();
        final CameraFragment cameraFragment = new CameraFragment();
        final UserFragment userFragment = new UserFragment();

        changeFragment(feedFragment);

        bnBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        changeFragment(feedFragment);
                        return true;
                    case R.id.action_new:
                        changeFragment(cameraFragment);
                        return true;
                    case R.id.action_user:
                        userFragment.setUser(ParseUser.getCurrentUser());
                        changeFragment(userFragment);
                        return true;
                }
                return false;
            }
        });
    }

    private void changeFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }
}
