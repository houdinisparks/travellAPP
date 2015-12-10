package com.example.zhenyang.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class Tab1 extends ListFragment implements View.OnClickListener{

    /** Set costs to 2 d.p. **/
    static DecimalFormat df = new DecimalFormat("###0.00");

    /** By default, include all destinations **/
    /** IMPORTANT: 1st tourist spot will be the starting and ending point **/
    private ArrayList<String> list = new ArrayList<String>();
    //private ArrayList<String> list = new ArrayList<String>(Arrays.asList("Marina Bay Sands","Singapore Flyer", "Vivo City", "Resorts World Sentosa", "Buddha Tooth Relic Temple", "Zoo"));

    /** Spinner options **/
    private ArrayList<String> options = new ArrayList<String>();

    /** Tourist Spot holder -- used for calculating optimal route **/
    private static ArrayList<TouristSpot> destinationTouristSpots = new ArrayList<TouristSpot>();

    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;
    public final static String EXTRA_MESSAGE = "com.example.zhenyang.MESSAGE";
    EditText budgetET;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_1,container,false);

        /** Add UI stuff to variables **/
        Button btn1 = (Button) v.findViewById(R.id.btnClear);
        Button btn2 = (Button) v.findViewById(R.id.btnGen);
        final Spinner spinnerDropDown = (Spinner) v.findViewById(R.id.spinner1);
        ListView listview = (ListView)v.findViewById(android.R.id.list);
        budgetET = (EditText) v.findViewById(R.id.budget);
        reinitialize();

        /** On-click event listener "Clear All" & "Generate Routes" */
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);

        /** Two ArrayAdapter to set Spinner items to ListView */
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
        adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, options);

        /** Setting adapters */
        this.setListAdapter(adapter);
        spinnerDropDown.setAdapter(adapter2);


        /** Long click to delete tourist spot entry **/
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                options.add(list.get(position));
                list.remove(position);
                adapter.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();
                return true;
            }
        });

        /** Add spinner entry to ListView **/
        spinnerDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) { // ignore first option
                    list.add(options.get(position));
                    Toast.makeText(getActivity(), "Added " + options.get(position), Toast.LENGTH_SHORT).show();
                    options.remove(position);
                    adapter.notifyDataSetChanged();
                    adapter2.notifyDataSetChanged();
                    spinnerDropDown.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        return v;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnClear:
                adapter.clear();
                reinitialize();
                break;
            case R.id.btnGen:
                String hello = budgetET.getText().toString();
                if ( hello.length() == 0 )
                    Variables.setBudget(0.0);
                else
                    Variables.setBudget(Double.parseDouble(budgetET.getText().toString()));
                generateRoute();
                break;
        }
    }

    public void reinitialize(){
        options.clear();
        options.add("Please select:");
        options.add("Marina Bay Sands");
        options.add("Singapore Flyer");
        options.add("Vivo City");
        options.add("Resorts World Sentosa");
        options.add("Buddha Tooth Relic Temple");
        options.add("Zoo");
    }
    /** Calculate route costs and stuff **/
    public void generateRoute()
    {
        String outputStr = "";

        // if list size is 0 => nothing to calculate
        // if list size is 1 => not enough to form a route
        if ( list.size() >= 2 ) {
            // Create and add Tourist Spots
            destinationTouristSpots.clear();
            for (String i : list)
                destinationTouristSpots.add(new TouristSpot(i));

            // Save the tourist spots into our route
            Route.setTouristSpots(destinationTouristSpots);

            // Initialize population
            // One population contains 50 routes
            // Repeat for 50 times: Randomly select 2 parent routes and produce 1 offspring route
            Population pop = new Population(50, true);
            pop = GeneticAlgo.evolvePopulation(pop);

            // Fast Approximate Solver
            Route bestRouteFAS = pop.getBestRoute();
            bestRouteFAS.upgradeTransportationMethods();

            outputStr = outputStr + "*************************\n" + "\nFAST APPROXIMATE SOLVER:\n" + "\n*************************\n\n"
                    + "Est. travel: " + bestRouteFAS.getTime() + " mins\n"
                    + "Est. costs : $" + df.format(bestRouteFAS.getCost()) + "\n\n"
                    + "Travel path: \n" + bestRouteFAS + "\n\n"
                    + "Transport methods: \n" + bestRouteFAS.transportMethods() + "\n\n";

            // Exhaustive Enumeration
            String[] destinations = list.toArray(new String[list.size()]);
            String[] destinationsWithoutMBS = new String[destinations.length - 1];
            System.arraycopy(destinations, 1, destinationsWithoutMBS, 0, destinations.length - 1);
            ArrayList<String[]> permutations = Route.getPermutations(destinationsWithoutMBS);

            Route bestRouteEE = Route.getBestRouteEE(permutations);
            bestRouteEE.upgradeTransportationMethods();

            outputStr = outputStr + "\n*************************\n" + "\nEXHAUSTIVE ENUMERATION:\n" + "\n*************************\n\n"
                    + "Est. travel: " + bestRouteEE.getTime() + " mins\n"
                    + "Est. costs : $" + df.format(bestRouteEE.getCost()) + "\n\n"
                    + "Travel path: \n" + bestRouteEE + "\n\n"
                    + "Transport methods: \n" + bestRouteEE.transportMethods();
        }
        else if (list.size() == 1)
            outputStr = "Only 1 tourist spot chosen\n 2 or more required to form a route.";
        else
            outputStr = "No tourist spot chosen";
        /** Display optimal route in another activity **/
        Intent intent = new Intent(getActivity().getApplicationContext(), DisplayBestRoute.class);
        intent.putExtra(EXTRA_MESSAGE, outputStr);
        startActivity(intent);

    }

}
