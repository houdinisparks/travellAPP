package com.example.zhenyang.app;

import android.content.Context;
import android.content.res.AssetManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tab2 extends Fragment implements View.OnClickListener {

    BKTree bktree;
    HashMap<String, Integer> suggestions;
    ArrayList<String> generatedList;
    ArrayList<String> oldList;
    AutoCompleteTextView autoCompleteText;
    ArrayAdapter<String> adaptor;
    ArrayList<ArrayList<String>> checkingList;
    ArrayList<ArrayList<String>> oldCheckingList;
    ArrayList<ArrayList<String>> storeList;
    String[] countries;
    int count;
    int countCopy;
    int allowFilter;

    private GoogleMap googleMap;
    double lat;
    double lon;

    MapView mapView;
    GoogleMap map;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_2, container, false);

        bktree = createBkTree();
        allowFilter = 0;

        checkingList = new ArrayList<ArrayList<String>>();
        oldCheckingList = new ArrayList<ArrayList<String>>();
        storeList = new ArrayList<ArrayList<String>>(5);


        autoCompleteText = (AutoCompleteTextView) v.findViewById(R.id.autocomplete_country);

        countries = getResources().getStringArray(R.array.countries_array);
        storeList.add(0, null);
        storeList.add(1, new ArrayList<String>(Arrays.asList(countries)));

        adaptor = new KArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<String>(Arrays.asList(countries)));
        autoCompleteText.setAdapter(adaptor);
        autoCompleteText.setThreshold(3);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        googleMap = mapFragment.getMap();

        final Button btnBiscuit = (Button) v.findViewById(R.id.btnBiscuit);
        btnBiscuit.setOnClickListener(this);

        return v;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBiscuit:

                String locationString = autoCompleteText.getText().toString();
                System.out.println(autoCompleteText.getText().toString());
                Geocoder myGcdr = new Geocoder(getContext());
                List<Address> matchedList = null; // list that will contain instances of class Address

                try {
                    Toast.makeText(getContext(), "Loading...", Toast.LENGTH_SHORT).show();
                    matchedList = myGcdr.getFromLocationName(locationString, 1); // pass in the string that user entered, and an integer of the number of addresses you want the list to contain
                } catch (IOException e) {
                    Toast.makeText(getContext(), "Not able to find location" + e.getMessage(), Toast.LENGTH_LONG).show();
                }

                try {
                    System.out.println(matchedList.get(0));
                    lat = matchedList.get(0).getLatitude();
                    lon = matchedList.get(0).getLongitude();
                    LatLng LATLON = new LatLng(lat, lon);
                    Toast.makeText(getContext(), "Latitude: " + lat + "\nLongitude: " + lon, Toast.LENGTH_LONG).show();
                    googleMap.clear();
                    googleMap.addMarker(new MarkerOptions().position(LATLON).title(locationString));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LATLON, 15));

                } catch (Exception e) {
                    Toast.makeText(getContext(), "A problem occured in retrieving location", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public BKTree createBkTree() {
        BKTree bktree = new BKTree();
        AssetManager assetManager = getActivity().getAssets();
        String line;

        try {
            InputStream iS = assetManager.open("dictionary.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(iS));

            while ((line = bufferedReader.readLine()) != null) {
                System.out.println("Added to BKTree : " + line);
                String[] splitline = line.split(" ");
                System.out.println("Added to BKTree : " + Arrays.toString(splitline));

                for (String i : splitline)
                    if (i.length() > 3) {
                        String smth = i.replaceAll("\\s", "");
                        bktree.add(smth);
                    }
                //Toast.makeText(this, line, Toast.LENGTH_SHORT).show();
            }
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            Toast.makeText(getActivity(), " Unable to open file! ", Toast.LENGTH_SHORT).show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return bktree;
    }

    public ArrayList<String> toList(HashMap<String, Integer> suggestions) {

        int j = 0;
        ArrayList<String> list = new ArrayList<String>();

        for (Map.Entry<String, Integer> i : suggestions.entrySet()) {
            list.add(i.getKey());
            j += 1;
        }

        return list;

    }


    public boolean changeText(String constraint) {

        String[] listofwords = constraint.split(" ");
        checkingList.clear();

        for (String i : listofwords) {
            suggestions = bktree.query(i, 3);
            generatedList = toList(suggestions);
            checkingList.add(generatedList);

        }

        System.out.println("Checking list: " + checkingList.toString() + "\n Old Checking List : " + oldCheckingList.toString() + "\n Are they equals to each other? " +
                checkingList.toString().equals(oldCheckingList.toString()));

        if (!checkingList.toString().equals(oldCheckingList.toString()) && checkingList != null) {
            System.out.println("Filtering the words " + checkingList);
            oldCheckingList = new ArrayList<>(checkingList);
            return true;
        } else {
            return false;
        }
    }

    public class KArrayAdapter extends ArrayAdapter<String> implements Filterable {

        private Context context;
        private Filter filter = new CustomFilter();
        private ArrayList<String> listAttractions;

        public KArrayAdapter(Context context, int viewResourceId, ArrayList<String> objects) {
            super(context, viewResourceId, objects);
            this.listAttractions = objects;
            this.context = context;

        }

        @Override
        public Filter getFilter() {
            return filter;
        }

        private class CustomFilter extends Filter {

            private ArrayList<String> filterSuggestions = new ArrayList<String>();
            private ArrayList<String> previousFilterSuggestions = new ArrayList<String>();
            boolean firstTime = true;

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                String[] constraintSplit = constraint.toString().split(" ");
                count = constraintSplit.length;
                boolean state = changeText(constraint.toString());
                System.out.println("Allowerd to filter? " + state + "List to filter : " + checkingList);

                int i = 0;
                int j = 0;

                for (String places : storeList.get(count)) {
                    //          System.out.println(places.toLowerCase() + " " + constraintSplit[count - 1] + " " + places.toLowerCase().contains(constraintSplit[count - 1].toLowerCase()));

                    if (i == 0 && (places.toLowerCase().contains(constraintSplit[count - 1].toLowerCase()) || state)) {
                        filterSuggestions.clear();
                        System.out.println("filterSuggesions Cleared? " + filterSuggestions);
                        i += 1;
                    }

                    if (places.toLowerCase().contains(constraintSplit[count - 1].toLowerCase())) {
                        filterSuggestions.add(places);
                        System.out.println("Adding " + places + "with " + constraint);
                        if (j == 0)
                            filterSuggestions.clear();
                        j = 1;
                    } else if (state && j == 0) {

                        for (String suggested : checkingList.get(count - 1)) {
                            if (places.toLowerCase().contains(suggested.toLowerCase()) && !filterSuggestions.contains(places)) {
                                filterSuggestions.add(places);
                                System.out.println("The places added are : " + places + " with the constriaint : " + suggested);

                            }
                        }
                    }
                }


                System.out.println("The count is : " + count + "store list is " + storeList.get(count));

                System.out.println(previousFilterSuggestions + " " + filterSuggestions + " " + !previousFilterSuggestions.equals(filterSuggestions));
                if (!previousFilterSuggestions.equals(filterSuggestions)) {
                    storeList.add(count + 1, new ArrayList<String>(filterSuggestions));
                }

                System.out.println("The filtered Results are :" + filterSuggestions);
                System.out.println("The new storedlist is " + storeList.get(count + 1));

                previousFilterSuggestions = new ArrayList<>(filterSuggestions);


                filterResults.values = filterSuggestions;
                filterResults.count = filterSuggestions.size();


                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listAttractions = (ArrayList<String>) results.values;
                notifyDataSetChanged();
                clear();


                System.out.println("listAttractions : " + listAttractions);
                if (listAttractions != null) {
                    for (int i = 0; i < listAttractions.size(); i++) {
                        // System.out.println(listAttractions);
                        add(listAttractions.get(i));
                        notifyDataSetInvalidated();
                    }



/*                if(firstTime && listAttractions != null) {
                    for (int i = 0; i < listAttractions.size(); i++) {
                        // System.out.println(listAttractions);
                        add(listAttractions.get(i));
                        notifyDataSetInvalidated();
                    }
                    firstTime = false;
                }

                if (!previousFilterSuggestions.equals(filterSuggestions)) {
                    clear();
                    if (listAttractions != null) {
                        for (int i = 0; i < listAttractions.size(); i++) {
                            // System.out.println(listAttractions);
                            add(listAttractions.get(i));
                            notifyDataSetInvalidated();
                        }

                    }

                }*/

                }

            }
        }
    }

}
