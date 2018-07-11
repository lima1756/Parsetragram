package com.ivanmorett.parsetragram.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ivanmorett.parsetragram.GlideApp;
import com.ivanmorett.parsetragram.LoginActivity;
import com.ivanmorett.parsetragram.R;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserFragment extends Fragment{

    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @BindView(R.id.tvUserName) TextView tvUserName;
    @BindView(R.id.btnProfileButton) Button btnProfileButton;
    private boolean selfUser;
    private ParseUser user;

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


        this.selfUser = user == ParseUser.getCurrentUser();

        tvUserName.setText(user.getUsername());

        changeCounters(view, R.id.counterFollowers, 0);
        changeCounters(view, R.id.counterFollowing, 0);
        changeCounters(view, R.id.counterPosts, 0);


        if(selfUser){
            btnProfileButton.setText("LogOut");
        }
        else{
            btnProfileButton.setText("Follow");
        }

        GlideApp.with(getContext())
                .load(user.getParseFile("profileImg").getUrl())
                .circleCrop()
                .into(ivProfileImage);


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

    public void setUser(ParseUser user){
        this.user = user;
    }

    private void changeCounters(View view, int counterId, int count){
        TextView tvCount = view.findViewById(counterId).findViewById(R.id.tvCount);
        tvCount.setText(count+"");
    }
}
