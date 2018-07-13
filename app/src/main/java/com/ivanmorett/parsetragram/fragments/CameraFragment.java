package com.ivanmorett.parsetragram.fragments;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ivanmorett.parsetragram.GlideApp;
import com.ivanmorett.parsetragram.constants.Images;
import com.ivanmorett.parsetragram.controllers.BitmapController;
import com.ivanmorett.parsetragram.Models.Post;
import com.ivanmorett.parsetragram.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.wonderkiln.camerakit.CameraKitEventCallback;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraView;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CameraFragment extends Fragment{

    @BindView(R.id.camera) CameraView camera;
    @BindView(R.id.imgPreview) ImageView imgPreview;
    @BindView(R.id.llSubmit) LinearLayout llSubmit;
    @BindView(R.id.btnCamera) Button btnCamera;
    @BindView(R.id.etDescription) EditText etDescription;

    private static Bitmap image;

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, parent, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


    }

    @Override
    public void onResume() {
        super.onResume();
        camera.start();
    }

    @Override
    public void onPause() {
        camera.stop();
        super.onPause();
    }


    @OnClick(R.id.btnCamera)
    public void shootPhoto(){
        camera.captureImage(new CameraKitEventCallback<CameraKitImage>() {
            @Override
            public void callback(CameraKitImage cameraKitImage) {
                final Bitmap result = cameraKitImage.getBitmap();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        camera.stop();
                        camera.setVisibility(View.GONE);
                        imgPreview.setVisibility(View.VISIBLE);
                        llSubmit.setVisibility(View.VISIBLE);
                        btnCamera.setVisibility(View.GONE);
                        image  = BitmapController.scaleToFitWidth(result, 300);
                        imgPreview.setImageBitmap(image);
                    }
                });
            }
        });
    }

    @OnClick(R.id.btnRetake)
    public void cancel(){
        camera.setVisibility(View.VISIBLE);
        imgPreview.setVisibility(View.GONE);
        llSubmit.setVisibility(View.GONE);
        btnCamera.setVisibility(View.VISIBLE);
        camera.start();
        clearDescription();
    }


    @OnClick(R.id.btnSubmit)
    public void submit(){
        if(etDescription.getText().toString().equals("")){
            Toast.makeText(getContext(), "Please insert a caption", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            submitPost(etDescription.getText().toString(), BitmapController.saveToFile(getContext(), image));
        } catch (IOException e) {
            Toast.makeText(getContext(), "There was an error, please try again.", Toast.LENGTH_SHORT).show();
        }
        clearDescription();
    }

    public void submitPost(String description, File image){
        Post post = new Post();
        post.setDescription(description);
        post.setImage(new ParseFile(image));
        post.setUser(ParseUser.getCurrentUser());

        GlideApp.with(getContext())
                .load("https://image.ibb.co/cNvfMo/spinner.gif")
                .into(imgPreview);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Toast.makeText(getContext(), "Image Saved.", Toast.LENGTH_SHORT).show();
                cancel();
            }
        });
    }

    private void clearDescription(){
        etDescription.setText("");
        etDescription.clearFocus();
    }


}
