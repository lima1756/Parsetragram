package com.ivanmorett.parsetragram.adapters;

import android.content.Context;
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

import com.ivanmorett.parsetragram.GlideApp;
import com.ivanmorett.parsetragram.Models.Post;
import com.ivanmorett.parsetragram.R;
import com.ivanmorett.parsetragram.interfaces.ChangeableFragment;
import com.parse.ParseFile;

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

        GlideApp.with(context)
                .load(post.getImage().getUrl())
                .into(holder.ivPostImage);

        ParseFile profileImg = post.getUser().getParseFile("profileImg");
        GlideApp.with(context)
                .load(post.getUser().getParseFile("profileImg")!=null?profileImg.getUrl():null)
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.btnLike)
        public void like(){
            // TODO
        }

        @OnClick(R.id.btnComments)
        public void comments(){
            // TODO
        }

        @OnClick({R.id.ivPostUserImage, R.id.tvPostUsername})
        public void viewProfile(){
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                changeableFragment.changeToUserFragment(mPosts.get(position).getUser());
            }
        }

    }
}
