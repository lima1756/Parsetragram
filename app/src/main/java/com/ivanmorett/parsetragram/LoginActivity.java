package com.ivanmorett.parsetragram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.btnLogin) Button btnLogin;
    @BindView(R.id.btnSignUp) Button btnSignUp;
    @BindView(R.id.etUser) EditText etUser;
    @BindView(R.id.etPass) EditText etPass;
    private static final String TAG = "LoginActivity";
    @BindView(R.id.pbLogIn) ProgressBar pbLogIn;
    @BindView(R.id.pbSignUp) ProgressBar pbSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            launchHome();
            return;
        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                login(etUser.getText().toString(), etPass.getText().toString());
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp(etUser.getText().toString(), etPass.getText().toString());
            }
        });
    }

    public void launchHome(){
        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(i);
        finish();
    }

    public void login(String user, String pass){
        disableButton(btnLogin, pbLogIn);
        ParseUser.logInInBackground(user, pass, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e==null){
                    launchHome();
                }
                else {
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                }
                enableButton(btnLogin, pbLogIn, R.string.log_in);
            }
        });
    }

    public void signUp(final String userName, final String pass){
        ParseUser user = new ParseUser();
        user.setUsername(userName);
        user.setPassword(pass);
        disableButton(btnSignUp, pbSignUp);
        user.signUpInBackground(new SignUpCallback(){

            @Override
            public void done(ParseException e) {
                if(e==null){
                    login(userName, pass);
                }
                else{
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }
                enableButton(btnSignUp, pbSignUp, R.string.sign_up);
            }
        });
    }

    private void disableButton(Button btn, ProgressBar pb){
        btn.setClickable(false);
        pb.setVisibility(View.VISIBLE);
        btn.setText("");
    }

    private void enableButton(Button btn, ProgressBar pb, int textId){
        btn.setClickable(true);
        pb.setVisibility(View.GONE);
        btn.setText(textId);
    }
}
