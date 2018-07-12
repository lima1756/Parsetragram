package com.ivanmorett.parsetragram.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ivanmorett.parsetragram.GlideApp;
import com.ivanmorett.parsetragram.Models.Comment;
import com.ivanmorett.parsetragram.R;
import com.ivanmorett.parsetragram.constants.Images;
import com.ivanmorett.parsetragram.controllers.TimeFormatterController;
import com.parse.ParseFile;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{


    private List<Comment> mComments;
    private Context context;

    public CommentAdapter(List<Comment> comments){
        this.mComments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View commentView = inflater.inflate(R.layout.item_comment, parent, false);

        // Return a new holder instance
        CommentAdapter.ViewHolder viewHolder = new CommentAdapter.ViewHolder(commentView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = mComments.get(position);

        String username = comment.getUser().getUsername();
        SpannableStringBuilder caption = new SpannableStringBuilder(username + " " + comment.getComment());
        caption.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, username.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tvComment.setText(caption);

        holder.tvCommentStamp.setText(TimeFormatterController.getRelativeTimeAgo(comment.getUpdatedAt().toString()));

        ParseFile profileImg = comment.getUser().getParseFile("profileImg");
        GlideApp.with(context)
                .load(profileImg!=null?profileImg.getUrl(): Images.NO_PROFILE_IMG)
                .circleCrop()
                .into(holder.ivCommentProfileImage);

    }

    @Override
    public int getItemCount() { return mComments.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvComment) TextView tvComment;
        @BindView(R.id.tvCommentTimeStamp) TextView tvCommentStamp;
        @BindView(R.id.ivCommentProfileImage) ImageView ivCommentProfileImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}