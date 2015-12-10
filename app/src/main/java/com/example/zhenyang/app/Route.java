package com.example.zhenyang.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Route {

	boolean recalculateCostTime = false;

	int[] modeOfTransport;

    // Holds a list of tourist spots along the route
	private static ArrayList<TouristSpot> listOfTouristSpots = new ArrayList<TouristSpot>();
    private ArrayList<TouristSpot> route = new ArrayList<TouristSpot>();

    private int time = 0;
    private double cost = 0;

    // Constructs a blank route
    public Route()
    {
    	// + 1 for MBS as last destination
        for (int i = 0; i < listOfTouristSpots.size()+1; i++) route.add(null);
        setModeOfTransport();
    }
    public Route(String[] routeIn)
    {
    	route.add(null); // reserve space for MBS

    	for (int i = 0; i < routeIn.length; i++)
        {
        	route.add(new TouristSpot(routeIn[i]));
        }
    	route.add(null); // reserve space for MBS

    	setTouristSpot(0, listOfTouristSpots.get(0));
    	setTouristSpot(listOfTouristSpots.size(), listOfTouristSpots.get(0));
    	setModeOfTransport();
    }

    public void setModeOfTransport()
    {
    	modeOfTransport = new int[listOfTouristSpots.size()+1];
    	for (int i = 0; i < listOfTouristSpots.size(); i++)
        {
	    	// 1. public transport, 2. taxi, 3. walking
	    	if ( Variables.getBudget() < 5.00 )
	    		modeOfTransport[i] = 3; // if low budget, set default to walking
	    	else
	    		modeOfTransport[i] = 1; // if not (average or high budget), set default to public transport
        }
    }

    // Creates a random route
    public void generateIndividual()
    {
        // Get all tourist spots and add them to the route
        for (int touristspotIndex = 1; touristspotIndex < listOfTouristSpots.size(); touristspotIndex++)
        {
        	setTouristSpot(touristspotIndex, listOfTouristSpots.get(touristspotIndex));
        }

        // Randomize the order of tourist spots
        Collections.shuffle(route);

        int null1 = -1, null2 = -1;

        for (int touristspotIndex = 0; touristspotIndex < numberOfTouristSpots()+1; touristspotIndex++)
        {
        	if ( route.get(touristspotIndex) == null && null1 == -1 )
        		null1 = touristspotIndex;
        	else if ( route.get(touristspotIndex) == null && null1 != -1 )
        		null2 = touristspotIndex;
        }

        // set Marina Bay Sands as the first and last destination
        // 1) put MBS into null entries
        // 2) swap MBS positions with first and last entries

        setTouristSpot(null1, listOfTouristSpots.get(0));
        setTouristSpot(null2, listOfTouristSpots.get(0));

    	Collections.swap(route, null1, 0);
    	Collections.swap(route, null2, routeSize()-1);

    }


    // Gets the specific tourist spot from the route
    public TouristSpot getTouristSpot(int routePosition)
    {
        return route.get(routePosition);
    }

    // Sets a tourist spot on a specific position on the route
    public void setTouristSpot(int routePosition, TouristSpot touristspot)
    {
        route.set(routePosition, touristspot);
        time = 0;
    }

    // calculate how good a route is based on the total cost
    public double getFitness()
    {
        return 1/getCost();
    }

    // Gets the total travel time of the route
    public int getTime()
    {
        if (time == 0 || recalculateCostTime ) // recalculate journey time and cost after upgrading transportation methods
        {
        	int routeTime = 0;
            // Loop through our route's cities
            for (int touristspot=0; touristspot < listOfTouristSpots.size(); touristspot++)
            {
            	// traveling from
                TouristSpot fromTouristSpot = getTouristSpot(touristspot);
                // traveling to
                TouristSpot destinationTouristSpot;

                // Get the next tourist spot of the route
                if(touristspot < routeSize()-2)
                    destinationTouristSpot = getTouristSpot(touristspot+1);
                // last destination must be Marina Bay Sands i.e. TouristSpot[0]
                else
                    destinationTouristSpot = getTouristSpot(0);

                // Get the travel time between the two Tourist Spots
                routeTime += fromTouristSpot.timeTo(destinationTouristSpot, modeOfTransport[touristspot]);
            }
            time = routeTime;
        }
        return time;
    }

    // Gets the total travel cost of the route
    public double getCost()
    {
        if (cost == 0 || recalculateCostTime) // recalculate journey time and cost after upgrading transportation methods
        {
            double routeCost = 0;
            // Loop through our route's cities
            for (int touristspot=0; touristspot < listOfTouristSpots.size(); touristspot++) {
            	// traveling from
                TouristSpot fromTouristSpot = getTouristSpot(touristspot);
                // traveling to
                TouristSpot destinationTouristSpot;

                // Get the next tourist spot of the route
                if(touristspot < routeSize()-2)
                    destinationTouristSpot = getTouristSpot(touristspot+1);
                // last destination must be Marina Bay Sands i.e. TouristSpot[0]
                else
                    destinationTouristSpot = getTouristSpot(0);

                // Get the travel cost between the two Tourist Spots
                routeCost += fromTouristSpot.costTo(destinationTouristSpot, modeOfTransport[touristspot]);
            }
            cost = routeCost;
        }
        return cost;
    }

    public void upgradeTransportationMethods()
    {
    	double currCost;
    	double upgradeToBusFromWalkingCost;
    	double upgradeToTaxiFromWalkingCost;
    	double upgradeToTaxiFromBusCost;

    	recalculateCostTime = true;
        for (int i=0; i < listOfTouristSpots.size(); i++)
        {
            currCost = getCost(); // recalculate journey time and cost after upgrading transportation methods

            TouristSpot fromTouristSpot = getTouristSpot(i);
            TouristSpot destinationTouristSpot;

            if(i == routeSize()-2)
                destinationTouristSpot = getTouristSpot(0);
            else
                destinationTouristSpot = getTouristSpot(i+1);
            upgradeToBusFromWalkingCost = fromTouristSpot.costTo(destinationTouristSpot,1) - 0;
            upgradeToTaxiFromWalkingCost = fromTouristSpot.costTo(destinationTouristSpot,2) - 0;
            upgradeToTaxiFromBusCost = fromTouristSpot.costTo(destinationTouristSpot,2) - fromTouristSpot.costTo(destinationTouristSpot,1);

            // Get the next more expensive mode of transportation
            if (modeOfTransport[i] == 1) // curr mode: bus => new mode: taxi
            {
                if (currCost + upgradeToTaxiFromBusCost <= Variables.getBudget() ) // upgrade to taxi
                    modeOfTransport[i] = 2;
            }
            if (modeOfTransport[i] == 3) // curr mode: walking => new mode: taxi/bus
            {
                if (currCost + upgradeToBusFromWalkingCost <= Variables.getBudget() ) // upgrade to bus
                    modeOfTransport[i] = 1;
                else if (currCost + upgradeToTaxiFromWalkingCost <= Variables.getBudget() ) // upgrade to taxi
                    modeOfTransport[i] = 2;
            }
        }
        getCost(); // update cost
    	recalculateCostTime = false;

    }

    public static Route getBestRouteEE(ArrayList<String[]> permutations)
    {
    	Route bestRoute = new Route(permutations.get(0));
        for ( String[] p : permutations )
    	{
    		Route newRoute = new Route(p);
    		if (bestRoute.getFitness() <= newRoute.getFitness()) {
            	bestRoute = newRoute;
            }
    	}
        return bestRoute;
    }

    // Get number of tourist spots on the route
    public int routeSize()
    {
        return route.size();
    }

    // Check if the route contains a tourist spot
    public boolean containsTouristSpot(TouristSpot touristspot)
    {
        return route.contains(touristspot);
    }

    public static void setTouristSpots(ArrayList<TouristSpot> list)
    {
    	listOfTouristSpots = list;
    }

    // Get the number of destination cities
    public static int numberOfTouristSpots(){
        return listOfTouristSpots.size();
    }

    public static ArrayList<String[]> getPermutations(String[] inputArray)
    {
        ArrayList<String[]> outputList = new ArrayList<String[]>();
        int length = inputArray.length;
        if ( length == 0 ) return outputList;
        if ( length == 1 )
        {
            outputList.add( inputArray );
            return outputList;
        }

        String[] subClone = Arrays.copyOf( inputArray, length - 1);
        System.arraycopy( inputArray, 1, subClone, 0, length - 1 );

        for ( int i = 0; i < length; ++i ){
            String e = inputArray[i];
            if ( i > 0 ) subClone[i-1] = inputArray[0];

            final ArrayList<String[]> subPermutations = getPermutations( subClone );
            for ( String[] sc : subPermutations )
            {
                String[] clone = Arrays.copyOf( inputArray, length );
                clone[0] = e;
                System.arraycopy( sc, 0, clone, 1, length - 1 );

                outputList.add( clone );
            }
            if ( i > 0 ) subClone[i-1] = e;
        }
        return outputList;
    }

    @Override
    // output order of tourist spots on the route
    public String toString()
    {
        String routeStr = "";
        for (int i = 0; i < routeSize(); i++) {
            routeStr += getTouristSpot(i)+" -> ";
        }
        return routeStr.substring(0, routeStr.length()-4);
    }

    // output list of transportation methods used
    public String transportMethods()
    {
        String routeStr = "";
        for (int i = 0; i < listOfTouristSpots.size(); i++)
        {
        	if ( modeOfTransport[i] == 1 )
        		routeStr += "Public Transport -> ";
        	if ( modeOfTransport[i] == 2 )
        		routeStr += "Taxi -> ";
        	if ( modeOfTransport[i] == 3 )
        		routeStr += "Walk -> ";
        }
        return routeStr.substring(0, routeStr.length()-4);
    }
}