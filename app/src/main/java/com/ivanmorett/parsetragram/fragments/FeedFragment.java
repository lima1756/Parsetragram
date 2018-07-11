package com.ivanmorett.parsetragram.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ivanmorett.parsetragram.Models.Post;
import com.ivanmorett.parsetragram.R;
import com.ivanmorett.parsetragram.adapters.PostAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedFragment extends Fragment{

    @BindView(R.id.rvFeed) RecyclerView rvFeed;
    @BindView(R.id.swipeFeed) SwipeRefreshLayout swipeFeed;
    private PostAdapter adapter;
    private ArrayList<Post> posts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, parent, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        posts = new ArrayList<>();
        adapter = new PostAdapter(posts);

        rvFeed.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFeed.setAdapter(adapter);



        swipeFeed.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateFeed();
            }
        });
        
        populateFeed();
    }

    private void populateFeed(){
        swipeFeed.setRefreshing(true);
        new Post.Query().getTop().withUser().findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e==null){
                    posts.clear();
                    posts.addAll(objects);
                    adapter.notifyDataSetChanged();
                }
                else{
                    e.printStackTrace();
                }
               swipeFeed.setRefreshing(false);
            }
        });
    }

}
