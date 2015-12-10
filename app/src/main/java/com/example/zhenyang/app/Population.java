package com.example.zhenyang.app;

public class Population {

    // Holds all the routes
    Route[] routes;

    public Population(int populationSize, boolean init) {
        routes = new Route[populationSize];
        if (init) {
            for (int i = 0; i < populationSize(); i++) {
                Route newRoute = new Route();
                newRoute.generateIndividual();
                saveRoute(i, newRoute);
            }
        }
    }
    
    public void saveRoute(int index, Route route) {
        routes[index] = route;
    }
    
    public Route getRoute(int index) {
        return routes[index];
    }

    // Gets the best route in the population
    public Route getBestRoute() {
        Route bestRoute = routes[0];
        // Loop through individual routes to find best route
        for (int i = 1; i < populationSize(); i++) {
            if (bestRoute.getFitness() <= getRoute(i).getFitness()) {
            	bestRoute = getRoute(i);
            }
        }
        return bestRoute;
    }

    // Gets population size
    public int populationSize() {
        return routes.length;
    }
}