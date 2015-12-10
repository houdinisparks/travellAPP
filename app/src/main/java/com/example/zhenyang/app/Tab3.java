package com.example.zhenyang.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Tab3 extends Fragment implements View.OnClickListener{
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Budget = "budKey";
    public static final String Days = "dayKey";
    public static final String Wallet = "walKey";
    public static final String Food = "foodKey";
    public static final String Shop = "shopKey";
    public static final String Trans = "transKey";
    public static final String Other = "othKey";
    public static final String Spent = "spentKey";
    SharedPreferences sharedpreferences;
    public static final String MyPREFENCES="myprefs";



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.tab_3, container, false);
        Button edit = (Button) v.findViewById(R.id.edit);
        edit.setOnClickListener(this);

        Button addfood = (Button) v.findViewById(R.id.addfood);
        addfood.setOnClickListener(this);
        Button addtransport = (Button) v.findViewById(R.id.addtransport);
        addtransport.setOnClickListener(this);
        Button addshop = (Button) v.findViewById(R.id.addshop);
        addshop.setOnClickListener(this);
        Button addothers = (Button) v.findViewById(R.id.addothers);
        addothers.setOnClickListener(this);



        return v;
    }
    public void onPause(){
        super.onPause();

        sharedpreferences = this.getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = sharedpreferences.edit();
        TextView spending = (TextView) getView().findViewById(R.id.displayspent);
        TextView wallet = (TextView) getView().findViewById(R.id.displaywallet);
        TextView inpdays = (TextView) getView().findViewById(R.id.displaydays);
        TextView inpbudget = (TextView) getView().findViewById(R.id.displaybudget);
        TextView food = (TextView) getView().findViewById(R.id.displayfood);
        TextView transport = (TextView) getView().findViewById(R.id.displaytravel);
        TextView shopping = (TextView) getView().findViewById(R.id.displayshop);
        TextView other = (TextView) getView().findViewById(R.id.displayothers);
        String bud = inpbudget.getText().toString();
        String day = inpdays.getText().toString();
        String wal = wallet.getText().toString();
        String foo = food.getText().toString();
        String sho = shopping.getText().toString();
        String tra = transport.getText().toString();
        String oth = other.getText().toString();
        String spe = spending.getText().toString();
        edt.clear();
        edt.putString(Budget,bud);
        edt.putString(Wallet,wal);
        edt.putString(Days, day);
        edt.putString(Food,foo);
        edt.putString(Shop,sho);
        edt.putString(Trans,tra);
        edt.putString(Other,oth);
        edt.putString(Spent, spe);
        edt.apply();
    }


    public void onResume(){
        super.onResume();
        TextView spending = (TextView) getView().findViewById(R.id.displayspent);
        spending.setText(String.valueOf(spent()));
        TextView wallet = (TextView) getView().findViewById(R.id.displaywallet);
        wallet.setText(String.valueOf(wallet()));
        sharedpreferences = this.getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = sharedpreferences.edit();
        sharedpreferences.getString(Budget, null);
        sharedpreferences.getString(Wallet, null);
        sharedpreferences.getString(Days, null);
        sharedpreferences.getString(Food, null);
        sharedpreferences.getString(Shop, null);
        sharedpreferences.getString(Trans, null);
        sharedpreferences.getString(Other, null);
        sharedpreferences.getString(Spent, null);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if(requestCode==1 & resultCode==Activity.RESULT_OK){
            TextView inpdays = (TextView) getView().findViewById(R.id.displaydays);
            TextView inpbudget = (TextView) getView().findViewById(R.id.displaybudget);
            String result=data.getStringExtra("result");

            String results = data.getStringExtra("results");

            inpbudget.setText(results);


            inpdays.setText(result);
        }
        if(requestCode==1&resultCode==Activity.RESULT_CANCELED){
        }
        if(requestCode==2){
            TextView food = (TextView) getView().findViewById(R.id.displayfood);
            //String items = Food.;

            String foods = data.getStringExtra("item");
            //Toast.makeText(getContext(), foods, Toast.LENGTH_LONG).show();
            food.setText(foods);
            // The name of the file to open.

        }
        if(requestCode==3){
            TextView transport = (TextView) getView().findViewById(R.id.displaytravel);
            String travel = data.getStringExtra("transport");
            transport.setText(travel);
        }
        if(requestCode==4){
            TextView shopping = (TextView) getView().findViewById(R.id.displayshop);
            String shop = data.getStringExtra("shop");
            shopping.setText(shop);
        }
        if(requestCode==5){
            TextView other = (TextView) getView().findViewById(R.id.displayothers);
            String others = data.getStringExtra("other");
            other.setText(others);
        }


    }
    public double spent (){
        TextView food = (TextView) getView().findViewById(R.id.displayfood);
        TextView transport = (TextView) getView().findViewById(R.id.displaytravel);
        TextView shopping = (TextView) getView().findViewById(R.id.displayshop);
        TextView other = (TextView) getView().findViewById(R.id.displayothers);
        double total = Double.parseDouble(food.getText().toString())+Double.parseDouble(transport.getText().toString())+Double.parseDouble(shopping.getText().toString())+Double.parseDouble(other.getText().toString());
        return total;
    }

    public double wallet(){
        TextView budget = (TextView) getView().findViewById(R.id.displaybudget);
        double total = Double.parseDouble(budget.getText().toString())-spent();
        return total;
    }



    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit:
                Intent i = new Intent(getActivity(),TabNew3.class);
                startActivityForResult(i,1);

                //Intent gonew3 = new Intent(getActivity(), TabNew3.class);


                //getActivity().startActivity(gonew3);
                break;
            case R.id.addfood:
                Intent food = new Intent(getActivity(),Food.class);
                //startActivity(food);
                startActivityForResult(food,2);
                break;
            case R.id.addtransport:
                Intent transport = new Intent(getActivity(),Transport.class);
                startActivityForResult(transport,3);
                break;
            case R.id.addshop:
                Intent shop = new Intent(getActivity(),Shopping.class);
                startActivityForResult(shop,4);
                break;
            case R.id.addothers:
                Intent others = new Intent(getActivity(),Others.class);
                startActivityForResult(others,5);
                break;
        }
    }


}
