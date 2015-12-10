package com.example.zhenyang.app;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Function1 {
	
	/***************************************
	* 
	*     FUNCTION 1: MAIN                 *
	*                                      *
	***************************************/
	/***************************************
	* 
	*  FLOW OF PROGRAM:
	*  
	*  1.1) Optimize route based on priority
	*  		// time => taxi as main transport
	*  		// cost => walking as main transport
	*  		// balance between time and cost => public transport as main transport
	*  1.2) Run randomization and use the best "fittest" (best time/ cost) candidate route out of a list of routes
	*  1.3) Pass "fittest" candidate through more rounds of trials.
	*  
	*  2) Upgrade certain routes' timing using cost as limiting factor
	* 		// e.g. if total cost of current route is < travel budget $$, upgrade from bus/walk to taxi
	*  
	***************************************/

	private static ArrayList<TouristSpot> destinationTouristSpots = new ArrayList<TouristSpot>();
	static DecimalFormat df = new DecimalFormat("###0.00");
	
	/*** IMPORTANT *** MAKE SURE MBS IS THE 1ST TOURIST SPOT */
	static String[] destinations = {"Marina Bay Sands","Singapore Flyer","Vivo City","Resorts World Sentosa","Buddha Tooth Relic Temple","Zoo"};
	
	public static void main(String[] args) {

        // Create and add Tourist Spots
		for (String i: destinations)
			destinationTouristSpots.add(new TouristSpot(i));
        
        // Save the tourist spots into our route
        Route.setTouristSpots(destinationTouristSpots);
        
        // Initialize population
        // One population contains 50 routes
        // Repeat for 10 times: Randomly select 2 parent routes and produce 1 offspring route
        Population pop = new Population(50, true);
        // Evolve population for 10 generations
        pop = GeneticAlgo.evolvePopulation(pop);
        

        // Fast Approximate Solver
        Route bestRouteFAS = pop.getBestRoute();
        bestRouteFAS.upgradeTransportationMethods();

        System.out.println("Fast Approximate Solver using evolution:\n");
        System.out.println("Est. travel: " + bestRouteFAS.getTime() + " mins");
        System.out.println("Est. costs : $" + df.format(bestRouteFAS.getCost()));
        System.out.println("Travel path: " + bestRouteFAS);
        System.out.println(bestRouteFAS.transportMethods());
        
        // Exhaustive Enumeration
        String[] destinationsWithoutMBS = new String[destinations.length-1];
        System.arraycopy(destinations, 1, destinationsWithoutMBS, 0, destinations.length-1);
        ArrayList<String[]> permutations = Route.getPermutations( destinationsWithoutMBS );
        Route bestRouteEE = Route.getBestRouteEE(permutations);
        bestRouteEE.upgradeTransportationMethods();

        System.out.println("\nExhaustive Enumeration:\n");
        System.out.println("Est. travel: " + bestRouteEE.getTime() + " mins");
        System.out.println("Est. costs : $" + df.format(bestRouteEE.getCost()));
        System.out.println("Travel path: " + bestRouteEE);
        System.out.println(bestRouteEE.transportMethods());

    }
}
