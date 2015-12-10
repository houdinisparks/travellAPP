package com.example.zhenyang.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

public class DisplayBestRoute extends AppCompatActivity {

    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the text view as the activity layout
        setContentView(R.layout.activity_display_best_route);

        // Get the message from the intent
        Intent intent = getIntent();
        final String message = intent.getStringExtra(Tab1.EXTRA_MESSAGE);
        ((TextView) findViewById(R.id.bestRouteStr)).setText(message);
        ((TextView) findViewById(R.id.bestRouteStr)).setMovementMethod(new ScrollingMovementMethod());

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
