package com.ivanmorett.parsetragram.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ivanmorett.parsetragram.Models.Post;
import com.ivanmorett.parsetragram.R;
import com.ivanmorett.parsetragram.adapters.PostAdapter;
import com.ivanmorett.parsetragram.interfaces.ChangeableFragment;
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
    private int page;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, parent, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        posts = new ArrayList<>();
        adapter = new PostAdapter(posts, (ChangeableFragment) getActivity());

        this.page = 0;
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvFeed.setLayoutManager(layoutManager);
        rvFeed.setAdapter(adapter);

        rvFeed.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d("onScrollStateChanged", newState+"");
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("onScrolled", dy+"");
                int pastVisiblesItems, visibleItemCount, totalItemCount;
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                    if((visibleItemCount + pastVisiblesItems) >= totalItemCount)
                    {
                        loadMorePosts();
                    }
                }
            }
        });



        swipeFeed.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateFeed(0);
            }
        });
        
        populateFeed(0);
    }

    private void loadMorePosts(){
        swipeFeed.setRefreshing(true);
        new Post.Query().setLimit(20)
                .getPage(page++)
                .orderByDescending("createdAt")
                .withUser().findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e==null){
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

    private void populateFeed(int page){
        this.page = page;
        posts.clear();
        loadMorePosts();
    }


}
