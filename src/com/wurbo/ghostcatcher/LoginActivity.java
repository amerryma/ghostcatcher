package com.wurbo.ghostcatcher;

import java.io.IOException;

//import com.google.android.gms.games.GamesClient;
//import com.google.android.gms.games.GamesClient.Builder;
import com.google.android.gms.common.*;
//import com.google.example.games.basegameutils.BaseGameActivity;
//import com.google.example.games.basegameutils.GameHelper;
//import com.google.example.games.basegameutils.GameHelper.GameHelperListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

    EditText userInput;
    EditText passInput;
    String user = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // beginUserInitiatedSignIn();

        userInput = (EditText) findViewById(R.id.userInput);
        passInput = (EditText) findViewById(R.id.passInput);

        TextView titleText = (TextView) findViewById(R.id.titleText);
        titleText.setTypeface(Typeface.createFromAsset(getAssets(),
                "gooddog.otf"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main);
        }
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_main);
        }
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void login(View view) {
        boolean error = false;
        try {
            String user = userInput.getText().toString();
            String pass = passInput.getText().toString();
            // Log.v("ghosthunter", user + "," + pass);
            if (HttpHelper.validateLogin(user, pass)) {
                Toast.makeText(getApplicationContext(),
                        "Logged in as " + user + ".", Toast.LENGTH_SHORT)
                        .show();
                this.user = user;
                startGame();
            } else {
                error = true;
            }
            userInput.clearComposingText();
            passInput.clearComposingText();
        } catch (IOException e) {
            error = true;
        }
        if (error) {
            Toast.makeText(getApplicationContext(),
                    "Bad connection or password.", Toast.LENGTH_SHORT).show();
        }
    }

    public void playAsGuest(View view) {
        this.user = "";
        startGame();
    }

    private void startGame() {
        // Hide keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(passInput.getWindowToken(), 0);

        Intent intent = new Intent(LoginActivity.this, GameActivity.class);
        intent.putExtra("username", user);
        startActivity(intent);
    }

    public void highscores(View view) {
        String LEADERBOARD_ID = getApplicationContext().getString(
                R.string.leaderboard_largest_size);
        int REQUEST_LEADERBOARD = 1010928318;

        // getGamesClient().submitScore(LEADERBOARD_ID, 1337);
        // startActivityForResult(getGamesClient().getLeaderboardIntent(LEADERBOARD_ID),
        // 5001);
        // startActivityForResult(getGamesClient().getLeaderboardIntent(LEADERBOARD_ID),
        // REQUEST_LEADERBOARD);
        // Intent intent = new
        // Intent(LoginActivity.this,HighscoreActivity.class);
        // startActivity(intent);
    }

    public void onSignInFailed() {
        Toast.makeText(getApplicationContext(), "Please Login",
                Toast.LENGTH_SHORT).show();

    }

    public void onSignInSucceeded() {
        Toast.makeText(getApplicationContext(), "Welcome Back",
                Toast.LENGTH_SHORT).show();

    }

}
