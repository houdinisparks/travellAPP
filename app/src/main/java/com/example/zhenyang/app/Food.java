package com.example.zhenyang.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static java.lang.Math.round;



public class Food extends Activity {
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private ArrayList<String> newcost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food2);
        // ADD HERE
        newcost = new ArrayList<>();
        lvItems = (ListView) findViewById(R.id.lvItems);

        items = new ArrayList<String>();
        readItems();
        newcost = getArray();




        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);


        lvItems.setAdapter(itemsAdapter);

        // Setup remove listener method call
        setupListViewListener();
    }
    private double sumFood (ArrayList<String> newcost){
        double sum = 0;
        for(int i = 0; i<newcost.size();i++){

            sum += Double.parseDouble(newcost.get(i));

        }
        return sum;
    }



    // Attaches a long click listener to the listview
    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        // Remove the item within array at position
                        removedel(pos);
                        items.remove(pos);

                        // Refresh the adapter

                        //Toast.makeText(getBaseContext(), "newcost"+newcost.toString(), Toast.LENGTH_LONG).show();


                        itemsAdapter.notifyDataSetChanged();
                        // Return true consumes the long click event (marks it handled)

                        writeItems();


                        return true;
                    }

                });

    }
    private void removedel (int pos){
        String s = items.get(pos);
        char[] c = s.toCharArray();
        String h="";
        for (int i =0; i<c.length-1;i++){
            if(c[i]==' ' & c[i+1]=='-'){
                break;
            }
            else if(c[i+1]!=' '){
                h+=c[i+1];

            }
        }


        newcost.remove(newcost.indexOf(h));

        saveArray();

    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etItem);
        EditText etNewPrice = (EditText) findViewById(R.id.etPrice);
        if(etNewPrice.getText().toString()==null||etNewPrice.getText().toString().trim().equals("")){
            Toast.makeText(getBaseContext(), "No Input for Cost", Toast.LENGTH_LONG).show();
        }
        else {
            String itemText = "$" + etNewPrice.getText().toString() + " - " + etNewItem.getText().toString();
            itemsAdapter.add(itemText);
            etNewItem.setText("");
            writeItems();
            newcost.add(etNewPrice.getText().toString());

            etNewPrice.setText("");
            Toast.makeText(getBaseContext(), "Long Press to Delete Entry", Toast.LENGTH_SHORT).show();

        }
    }



        private void readItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "foods.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }

    }


    private void writeItems() {

        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "foods.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void onBackPressed(){
        readItems();
        saveArray();


        Intent foodint = new Intent();
        String foodstring = String.valueOf(round(sumFood(newcost)*100.0)/100.0);


        foodint.putExtra("item",foodstring);
        setResult(Activity.RESULT_OK, foodint);
        finish();


    }

    private static final String SHARED_PREFS_NAME = "MY_SHARED_PREFS";

    private boolean saveArray() {
        SharedPreferences sp = this.getSharedPreferences(SHARED_PREFS_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor mEdit1 = sp.edit();
        HashSet<String> set = new LinkedHashSet<String>();

        set.addAll(newcost);

        mEdit1.putStringSet("list", set);

        return mEdit1.commit();
    }

    private ArrayList<String> getArray() {
        SharedPreferences sp = this.getSharedPreferences(SHARED_PREFS_NAME, Activity.MODE_PRIVATE);


        //NOTE: if shared preference is null, the method return empty Hashset and not null
        Set<String> set = sp.getStringSet("list", new LinkedHashSet<String>());



        return new ArrayList<String>(set);

    }




}