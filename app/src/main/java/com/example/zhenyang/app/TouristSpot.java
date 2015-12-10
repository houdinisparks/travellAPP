package com.example.zhenyang.app;

public class TouristSpot {
    final String name;
    final DataCollection timecostMap;
    
    // Constructs a Tourist Spot
    public TouristSpot(String name)
    {
        this.name = name;
        this.timecostMap = new DataCollection(name);
    }

    /*****************************
	 * int modeOfTransport       *
	 * 1 - Public Transportation *
	 * 2 - Taxi                  *
	 * 3 - Walk                  *
	 ****************************/
    
    public int timeTo(TouristSpot touristspot, int modeOfTransport)
    {
        int time = this.timecostMap.getTimeTo(touristspot.name, modeOfTransport);
        return time;
    }
    
    public double costTo(TouristSpot touristspot, int modeOfTransport)
    {
    	double cost = this.timecostMap.getCostTo(touristspot.name, modeOfTransport);
    	return cost;
    }
    public String getName()
    {
    	return name;
    }
    
    @Override
    public String toString(){
        return getName();
    }
}