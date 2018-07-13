package com.ivanmorett.parsetragram.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ivanmorett.parsetragram.CommentsActivity;
import com.ivanmorett.parsetragram.GlideApp;
import com.ivanmorett.parsetragram.Models.Post;
import com.ivanmorett.parsetragram.R;
import com.ivanmorett.parsetragram.constants.Images;
import com.ivanmorett.parsetragram.controllers.TimeFormatterController;
import com.ivanmorett.parsetragram.interfaces.ChangeableFragment;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    private List<Post> mPosts;
    private Context context;
    private ChangeableFragment changeableFragment;

    public PostAdapter(List<Post> mPosts, ChangeableFragment changeableFragment){
        this.mPosts = mPosts;
        this.changeableFragment = changeableFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View postView = inflater.inflate(R.layout.item_post, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = mPosts.get(position);

        String username = post.getUser().getUsername();
        holder.tvPostUsername.setText(username);

        SpannableStringBuilder caption = new SpannableStringBuilder(username + " " + post.getDescription());
        caption.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, username.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tvPostDescription.setText(caption);

        holder.tvPostDate.setText(TimeFormatterController.getRelativeTimeAgo(post.getUpdatedAt().toString()));

        holder.tvLikeCount.setText(post.getLikesCount()+"");

        if(post.isLiked()){
            holder.btnLike.setBackground(context.getDrawable(R.drawable.ufi_heart_active));
            holder.btnLike.setBackgroundTintList(context.getResources().getColorStateList(R.color.red_5));
        }
        else{
            holder.btnLike.setBackground(context.getDrawable(R.drawable.ufi_heart));
            holder.btnLike.setBackgroundTintList(context.getResources().getColorStateList(R.color.black));
        }

        GlideApp.with(context)
                .load(post.getImage().getUrl())
                .into(holder.ivPostImage);

        ParseFile profileImg = post.getUser().getParseFile("profileImg");
        GlideApp.with(context)
                .load(profileImg!=null?profileImg.getUrl(): Images.NO_PROFILE_IMG)
                .circleCrop()
                .into(holder.ivPostUserImage);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.ivPostUserImage) ImageView ivPostUserImage;
        @BindView(R.id.tvPostUsername) TextView tvPostUsername;
        @BindView(R.id.ivPostImage) ImageView ivPostImage;
        @BindView(R.id.btnLike) Button btnLike;
        @BindView(R.id.btnComments) Button btnComments;
        @BindView(R.id.tvPostDescription) TextView tvPostDescription;
        @BindView(R.id.tvPostDate) TextView tvPostDate;
        @BindView(R.id.tvLikeCount) TextView tvLikeCount;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.btnLike)
        public void like(){
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Post post = mPosts.get(position);
                if(post.isLiked())
                    post.removeLike(ParseUser.getCurrentUser().getObjectId());
                else
                    post.addLike(ParseUser.getCurrentUser().getObjectId());
                post.saveInBackground();
                notifyItemChanged(position);

            }

        }

        @OnClick({R.id.btnComments, R.id.ivPostImage})
        public void comments(){
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Intent commentActivity = new Intent(context, CommentsActivity.class);
                commentActivity.putExtra("post", mPosts.get(position));
                context.startActivity(commentActivity);
            }
        }

        @OnClick({R.id.ivPostUserImage, R.id.tvPostUsername})
        public void viewProfile(){
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                changeableFragment.changeToUserFragment(mPosts.get(position).getUser());
            }
        }

        @OnClick(R.id.tvPostDescription)
        public void displayAllText(){
            tvPostDescription.setMaxLines(Integer.MAX_VALUE);
            tvPostDescription.setEllipsize(null);
        }

    }

}
