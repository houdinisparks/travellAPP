package com.example.zhenyang.app;

public class Cost {

	private final double cost_public;
	private final double cost_taxi;
	
    private final int time_public;
    private final int time_taxi;
    private final int time_walk;
    

    public Cost(double cost_public, int time_public, double cost_taxi, int time_taxi, int time_walk) {
        this.cost_public = cost_public; this.time_public = time_public;
        this.cost_taxi = cost_taxi; this.time_taxi = time_taxi;
        this.time_walk = time_walk;
    }
    
    public String toString(){
    	String rtnStr = String.format( "Public Transport: $%.2f / %d mins\nTaxi: $%.2f / %d mins\nWalk: %d mins\n", cost_public, time_public, cost_taxi, time_taxi, time_walk);
    	return rtnStr;
    }
    
    public double getCostPublic() { return cost_public; }
    public double getCostTaxi() { return cost_taxi; }
    
    public int getTimePublic() { return time_public; }
    public int getTimeTaxi() { return time_taxi; }
    public int getTimeWalk() { return time_walk; }
    
    
    @Override
    public boolean equals(Object o) {
    	if (!(o instanceof Cost)||o==null) return false;
        return this == o;
    }
    
    @Override
    public int hashCode() {
    	return time_public + time_taxi + time_walk;
    }

}
