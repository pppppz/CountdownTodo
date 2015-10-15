package com.app.countdowntodolist;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends Activity {

    Button.OnClickListener FacebookOnClick = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(LoginActivity.this, FacebookLoginActivity.class);
            startActivity(intent);
            finish();

        }
    };


    Button.OnClickListener TwitterOnClick = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(LoginActivity.this, TwitterLoginActivity.class);
            startActivity(intent);
            finish();

        }
    };
    private EditText UsernameField;
    private EditText PasswordField;
    private TextView ErrorField;
    private String Username;
    private String Password;
    private Button mBtnFb, mBtnTwitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        UI();
    }

    private void UI(){

        UsernameField = (EditText) findViewById(R.id.login_username);
        PasswordField = (EditText) findViewById(R.id.login_password);
        ErrorField = (TextView) findViewById(R.id.error_messages);

        mBtnFb = (Button) findViewById(R.id.facebook_login_button);
        mBtnFb.setOnClickListener(FacebookOnClick);

        mBtnTwitter = (Button) findViewById(R.id.twitter_login_button);
        mBtnTwitter.setOnClickListener(TwitterOnClick);

    }

    //click signIn for execute asyncTask login (in background)
    public void signIn(final View v){
        getID();
        new loginTask().execute();

    }

    //click register button for go to intent register page
     public void toRegistration(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    //insert menu_48
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_48; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    private void getID(){
        runOnUiThread(new Thread(new Runnable() {
            @Override
            public void run() {
                Username = String.valueOf(UsernameField.getText());
                Password = String.valueOf(PasswordField.getText());
            }
        }));
    }

    private class loginTask extends AsyncTask<Void, Integer, Void> {

        ProgressDialog pd;

        protected void onPreExecute() {

            pd = new ProgressDialog(LoginActivity.this);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setTitle("Login");
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.setIndeterminate(false);
            pd.setMax(100);
            pd.setProgress(0);
            pd.show();

        }

        protected Void doInBackground(Void... params) {
            ParseUser.logInInBackground(Username, Password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {

                    if (user != null) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Signup failed. Look at the ParseException to see what happened.
                        switch (e.getCode()) {
                            case ParseException.USERNAME_TAKEN:
                                ErrorField.setText("Sorry, this username has already been taken.");
                                break;
                            case ParseException.USERNAME_MISSING:
                                ErrorField.setText("Sorry, you must supply a username to register.");
                                break;
                            case ParseException.PASSWORD_MISSING:
                                ErrorField.setText("Sorry, you must supply a password to register.");
                                break;
                            case ParseException.OBJECT_NOT_FOUND:
                                ErrorField.setText("Sorry, those credentials were invalid.");
                                break;
                            default:
                                ErrorField.setText(e.getLocalizedMessage());
                                //  Log.e("Login Activity" , String.valueOf(e));
                                break;
                        }
                    }
                }
            });

            return null;
        }

        protected void onProgressUpdate(Integer... values) {
            pd.setProgress(values[0]);
        }

        protected void onPostExecute(Void result) {


        }
    }


}
