package com.wurbo.ghostcatcher;

import java.io.IOException;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class HighscoreActivity extends Activity {
    private ListView lv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_highscore);

        TextView titleText = (TextView) findViewById(R.id.textView1);
        titleText.setTypeface(Typeface.createFromAsset(getAssets(),
                "gooddog.otf"));
        try {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1,
                    HttpHelper.getHighscores());
            lv = (ListView) findViewById(R.id.highscoreView);
            lv.setAdapter(arrayAdapter);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_highscore, menu);
        return true;
    }

}
