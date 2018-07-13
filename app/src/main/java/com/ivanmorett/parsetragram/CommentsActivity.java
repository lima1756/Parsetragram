package com.ivanmorett.parsetragram;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ivanmorett.parsetragram.Models.Comment;
import com.ivanmorett.parsetragram.Models.Post;
import com.ivanmorett.parsetragram.adapters.CommentAdapter;
import com.ivanmorett.parsetragram.constants.Images;
import com.ivanmorett.parsetragram.controllers.TimeFormatterController;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommentsActivity extends AppCompatActivity {

    private int page;
    private CommentAdapter adapter;
    private ArrayList<Comment> comments;
    private Post post;
    @BindView(R.id.rvComments) RecyclerView rvComments;
    @BindView(R.id.swipeComments) SwipeRefreshLayout swipeComments;
    @BindView(R.id.etNewComment) EditText etNewComment;
    @BindView(R.id.tbComments) Toolbar tbComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ButterKnife.bind(this);

        // Views for the original post data
        View originalPost = findViewById(R.id.commentOriginalPost);
        TextView tvOriginalPost = originalPost.findViewById(R.id.tvComment);
        TextView tvTimeStamp = originalPost.findViewById(R.id.tvCommentTimeStamp);
        ImageView ivProfileImage = originalPost.findViewById(R.id.ivCommentProfileImage);

        // Retrieving original post data
        Intent i = getIntent();
        post = i.getParcelableExtra("post");

        // Configuring toolbar
        setSupportActionBar(tbComments);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setting original post data
        String originalUser = post.getUser().getUsername();
        SpannableStringBuilder caption = new SpannableStringBuilder(originalUser + " " + post.getDescription());
        caption.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, originalUser.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvOriginalPost.setText(caption);

        tvTimeStamp.setText(TimeFormatterController.getRelativeTimeAgo(post.getUpdatedAt().toString()));

        ParseFile profileImg = post.getUser().getParseFile("profileImg");
        GlideApp.with(getApplicationContext())
                .load(profileImg!=null?profileImg.getUrl(): Images.NO_PROFILE_IMG)
                .circleCrop()
                .into(ivProfileImage);

        // Initialization of data for comment list
        this.page = 0;
        this.comments = new ArrayList<>();
        this.adapter = new CommentAdapter(comments);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvComments.setLayoutManager(layoutManager);
        rvComments.setAdapter(adapter);

        rvComments.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int pastVisiblesItems, visibleItemCount, totalItemCount;
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                    if((visibleItemCount + pastVisiblesItems) >= totalItemCount)
                    {
                        loadMoreComments();
                    }
                }
            }
        });

        swipeComments.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateComments(0);
            }
        });

        populateComments(0);

    }

    private void loadMoreComments(){
        swipeComments.setRefreshing(true);
        new Comment.Query().setLimit(20)
                .getPage(page++)
                .orderByDescending("createdAt")
                .withUser()
                .contains("post", post.getObjectId())
                .findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> objects, ParseException e) {
                if(e==null){
                    comments.addAll(objects);
                    adapter.notifyDataSetChanged();
                }
                else{
                    e.printStackTrace();
                }
                swipeComments.setRefreshing(false);
            }
        });
    }

    private void populateComments(int page){
        this.page = page;
        comments.clear();
        loadMoreComments();
    }

    @OnClick(R.id.btnSubmitComment)
    public void postComment(){
        Comment comment = new Comment();
        comment.setUser(ParseUser.getCurrentUser());
        comment.setComment(etNewComment.getText().toString());
        comment.setPost(post);
        comment.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                populateComments(0);
                etNewComment.setText("");
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
