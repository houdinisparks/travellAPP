package com.example.zhenyang.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TabNew3 extends AppCompatActivity {

    //public final static String EXTRA_MESSAGE = "\"com.example.zhenyang.DAYS\"";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab3_1);
        Button confirm = (Button)findViewById(R.id.confirmbtn);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab3_1, menu);
        return true;
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

    public void goback (View v){
        EditText enter= (EditText) findViewById(R.id.daysinput);
        String result = enter.getText().toString();
        EditText money = (EditText) findViewById(R.id.budgetinput);
        String budget = money.getText().toString();
        if(budget==null||budget.trim().equals("")) {
            Toast.makeText(getBaseContext(), "No Input for Budget", Toast.LENGTH_LONG).show();
        }
        else {


            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", result);
            returnIntent.putExtra("results", budget);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }


    }
    public void onBackPressed(){





        setResult(Activity.RESULT_CANCELED);
        this.finish();
    }
}
