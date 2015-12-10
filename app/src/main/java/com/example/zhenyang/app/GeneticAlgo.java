package com.example.zhenyang.app;

public class GeneticAlgo {
    
    // Evolves a population of routes over 1 generation
    public static Population evolvePopulation(Population pop) {
        Population newPopulation = new Population(pop.populationSize(), false);

        // Keep the best route
        newPopulation.saveRoute(0, pop.getBestRoute());
        
        // Crossover population
        // Loop over the new population's size and create individuals from
        // Current population
        for (int i = 1; i < newPopulation.populationSize(); i++) {
            // Select parents
            Route parent1 = candidateSelection(pop);
            Route parent2 = candidateSelection(pop);
            // Crossover parents
            Route child = crossover(parent1, parent2);
            // Add child to new population
            newPopulation.saveRoute(i, child);
        }


        return newPopulation;
    }

    // Crossover between two parents to produce a combinational child
    public static Route crossover(Route parent1, Route parent2) {

        Route child = new Route();
        

        // Generate random start and end of subset of parent1
        int startPos = (int) (Math.random() * parent1.routeSize());
        int endPos = (int) (Math.random() * parent1.routeSize());

        // Add the above-mentioned subset into child's route
        for (int i = 0; i < child.routeSize(); i++) {
            // start position < end position
            if (startPos < endPos && i > startPos && i < endPos) {
                child.setTouristSpot(i, parent1.getTouristSpot(i));
            } // start position > end position
            else if (startPos > endPos) {
                if (!(i < startPos && i > endPos)) {
                    child.setTouristSpot(i, parent1.getTouristSpot(i));
                }
            }
        }
        
        // Fill in rest of child's route with corresponding parent2's route
        for (int i = 0; i < parent2.routeSize(); i++) {
            // If child doesn't have the city add it
            if (!child.containsTouristSpot(parent2.getTouristSpot(i))) {
                // Find 1 empty position to add tourist spot
                for (int ii = 0; ii < child.routeSize(); ii++) {
                    // empty position => add tourist spot (from parent2)
                    if (child.getTouristSpot(ii) == null) {
                        child.setTouristSpot(ii, parent2.getTouristSpot(i));
                        break;
                    }
                }
            }
        }
        // add last destination (Marina Bay Sands) to child
        child.setTouristSpot(child.routeSize()-1, parent2.getTouristSpot(0));
        
        return child;
    }

    // Selects candidate tour for crossover
    private static Route candidateSelection(Population pop) {

    	// Number of candidates to try
    	int numOfCandidates = 4;
    	
        Population candidates = new Population(numOfCandidates, false);
        
        for (int i = 0; i < numOfCandidates; i++) {
            int randomNum = (int) (Math.random() * pop.populationSize());
            candidates.saveRoute(i, pop.getRoute(randomNum));
        }
        
        Route bestRoute = candidates.getBestRoute();
        return bestRoute;
        
    }
}