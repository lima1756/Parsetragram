package com.ivanmorett.parsetragram.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ivanmorett.parsetragram.GlideApp;
import com.ivanmorett.parsetragram.LoginActivity;
import com.ivanmorett.parsetragram.Models.Post;
import com.ivanmorett.parsetragram.R;
import com.ivanmorett.parsetragram.adapters.ImagesAdapter;
import com.ivanmorett.parsetragram.constants.Images;
import com.ivanmorett.parsetragram.controllers.BitmapController;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserFragment extends Fragment{

    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @BindView(R.id.tvUserName) TextView tvUserName;
    @BindView(R.id.btnProfileButton) Button btnProfileButton;
    @BindView(R.id.gvUserPosts) GridView gvUserPosts;

    private boolean selfUser;
    private ParseUser user;
    private final static int PICK_IMAGE = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, parent, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        this.selfUser = ParseUser.getCurrentUser().getUsername().equals(user.getUsername());

        tvUserName.setText(user.getUsername());

        changeCounters(view, R.id.counterFollowers, 0, "Followers");
        changeCounters(view, R.id.counterFollowing, 0, "Following");
        changeCounters(view, R.id.counterPosts, 0, "Posts");


        if(selfUser){
            btnProfileButton.setText("LogOut");
        }
        else{
            btnProfileButton.setText("Follow");
        }

        ParseFile profileImg=this.user.getParseFile("profileImg");
        GlideApp.with(getContext())
                .load(profileImg!=null?profileImg.getUrl(): Images.NO_PROFILE_IMG)
                .circleCrop()
                .into(ivProfileImage);


        new Post.Query()
                .orderByDescending("createdAt")
                .filterByUser(user)
                .findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e==null){

                    ImagesAdapter imagesAdapter = new ImagesAdapter(getContext(), objects);
                    gvUserPosts.setAdapter(imagesAdapter);

                }
                else{
                    e.printStackTrace();
                }
            }
        });



    }

    @OnClick(R.id.btnProfileButton)
    public void btnClick(){
        if(selfUser){
            ParseUser.logOut();
            getActivity().startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        }else{
            Toast.makeText(getContext(), "Soon!", Toast.LENGTH_SHORT).show();
            // TODO
        }
    }

    @OnClick(R.id.ivProfileImage)
    public void changeProfileImage(){
        if(!selfUser)
            return;
        Intent galleryIntent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);


        startActivityForResult(galleryIntent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
                Bitmap bmp = BitmapFactory.decodeStream(inputStream);
                ParseUser currentUser = ParseUser.getCurrentUser();
                currentUser.put("profileImg", new ParseFile(BitmapController.saveToFile(getContext(), bmp)));
                currentUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null) {
                            ParseFile profileImg = ParseUser.getCurrentUser().getParseFile("profileImg");
                            GlideApp.with(getContext())
                                    .load(profileImg != null ? profileImg.getUrl() : Images.NO_PROFILE_IMG)
                                    .circleCrop()
                                    .into(ivProfileImage);
                        }
                        else{
                            e.printStackTrace();
                            Toast.makeText(getContext(), "There was an error, please try again later.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void setUser(ParseUser user){
        this.user = user;
    }

    private void changeCounters(View view, int counterId, int count, String description){
        ((TextView)view.findViewById(counterId).findViewById(R.id.tvCount)).setText(count+"");
        ((TextView)view.findViewById(counterId).findViewById(R.id.tvName)).setText(description);
    }
}
