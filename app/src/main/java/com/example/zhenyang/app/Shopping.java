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
import java.util.LinkedHashSet;
import java.util.Set;

public class Shopping extends Activity {
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private ArrayList<String> newcost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        // ADD HERE
        newcost = new ArrayList<>();
        lvItems = (ListView) findViewById(R.id.lvItems2);

        items = new ArrayList<String>();
        readItems();
        newcost = getArray();




        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);


        lvItems.setAdapter(itemsAdapter);

        // Setup remove listener method call
        setupListViewListener();
    }
    public double sumFood (ArrayList<String> newcost){
        double sum = 0;
        for(int i = 0; i<newcost.size();i++){
            sum+= Double.parseDouble(newcost.get(i));
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
                        removedel(pos);
                        // Remove the item within array at position
                        items.remove(pos);
                        // Refresh the adapter

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

    public void onAddItem2(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etItem2);
        EditText etNewPrice = (EditText) findViewById(R.id.etPrice2);
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
        File todoFile = new File(filesDir, "shop.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }

    }


    private void writeItems() {

        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "shop.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void onBackPressed(){
        readItems();
        saveArray();


        Intent shopint = new Intent();
        String shopstring = String.valueOf(sumFood(newcost));


        shopint.putExtra("shop", shopstring);
        setResult(Activity.RESULT_OK, shopint);
        finish();


    }

    private static final String SHARED_PREFS_NAME = "MY_SHARED_PREFS2";

    private boolean saveArray() {
        SharedPreferences sp = this.getSharedPreferences(SHARED_PREFS_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor mEdit1 = sp.edit();
        LinkedHashSet<String> set = new LinkedHashSet<String>();
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