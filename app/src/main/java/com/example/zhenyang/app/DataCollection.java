package com.example.zhenyang.app;

import java.util.HashMap;
import java.util.Map;

public class DataCollection {

	// TS: Tourist Spots
	final static String[] TS = {"Marina Bay Sands", "Singapore Flyer", "Vivo City", "Resorts World Sentosa",
			  "Buddha Tooth Relic Temple", "Zoo"};
	
	final Map<String, Cost> map = new HashMap<String, Cost>();
	
	/*****************************
	 * int modeOfTransport       *
	 * 1 - Public Transportation *
	 * 2 - Taxi                  *
	 * 3 - Walk                  *
	 ****************************/
	
	public int getTimeTo(String destination, int modeOfTransport)
	{
		if (modeOfTransport == 1) return map.get(destination).getTimePublic();
		else if (modeOfTransport == 2) return map.get(destination).getTimeTaxi();
		else if (modeOfTransport == 3) return map.get(destination).getTimeWalk();
		else return 0;
	}

	public double getCostTo(String destination, int modeOfTransport)
	{
		if (modeOfTransport == 1) return map.get(destination).getCostPublic();
		else if (modeOfTransport == 2) return map.get(destination).getCostTaxi();
		else return 0;
	}


	public DataCollection(String name) {
		if (name == "Marina Bay Sands")
		{
			
			/**************************
			*                         *
			*  From Marina Bay Sands  *
			*                         *
			**************************/
			
			// Cost Format: PublicTrans$1, Taxi$1, Foot
			map.put(TS[0], new Cost(0, 0, 0, 0, 0));
			map.put(TS[1], new Cost(0.83, 17, 3.22, 3, 14));
			map.put(TS[2], new Cost(1.18, 26, 6.96, 14, 69));
			map.put(TS[3], new Cost(4.03, 35, 8.50, 19, 76));
			map.put(TS[4], new Cost(0.88, 19, 4.98, 8, 28));
			map.put(TS[5], new Cost(1.96, 84, 18.40, 30, 269));
		}
		
		else if (name == "Singapore Flyer")
		{
			
			/**************************
			*                         *
			*  From Singapore Flyer   *
			*                         *
			**************************/
			
			map.put(TS[0], new Cost(0.83, 17, 4.32, 6, 14));
			map.put(TS[2], new Cost(1.26, 31, 7.84, 13, 81));
			map.put(TS[3], new Cost(4.03, 38, 9.38, 18, 88));
			map.put(TS[4], new Cost(0.98, 24, 4.76, 8, 39));
			map.put(TS[5], new Cost(1.89, 85, 18.18, 29, 264));
		}
		else if (name == "Vivo City")
		{
			
			/**************************
			*                         *
			*     From Vivo City      *
			*                         *
			**************************/
			
			map.put(TS[0], new Cost(1.18, 24, 8.30, 12, 69));
			map.put(TS[1], new Cost(1.26, 29, 7.96, 14, 81));
			map.put(TS[3], new Cost(2.00, 10, 4.54, 9, 12));
			map.put(TS[4], new Cost(0.98, 18, 6.42, 11, 47));
			map.put(TS[5], new Cost(1.99, 85, 22.58, 31, 270));
		}
		else if (name == "Resorts World Sentosa")
		{
			
			/*****************************
			*                            *
			* From Resorts World Sentosa *
			*                            *
			*****************************/
			
			map.put(TS[0], new Cost(1.18, 33, 8.74, 13, 76));
			map.put(TS[1], new Cost(1.26, 38, 8.40, 14, 88));
			map.put(TS[2], new Cost(0.00, 10, 3.22, 4, 12));
			map.put(TS[4], new Cost(0.98, 27, 6.64, 12, 55));
			map.put(TS[5], new Cost(1.99, 92, 22.80, 32, 285));
		}
		else if (name == "Buddha Tooth Relic Temple")
		{
			
			/*********************************
			*                                *
			* From Buddha Tooth Relic Temple *
			*                                *
			*********************************/
			
			map.put(TS[0], new Cost(0.88, 18, 5.32, 7, 28));
			map.put(TS[1], new Cost(0.98, 23, 4.76, 8, 39));
			map.put(TS[2], new Cost(0.98, 19, 4.98, 9, 47));
			map.put(TS[3], new Cost(3.98, 28, 6.52, 14, 55));
			map.put(TS[5], new Cost(1.91, 83, 18.40, 30, 264));
		}
		else if (name == "Zoo")
		{
			/**************************
			*                         *
			*        From Zoo         *
			*                         *
			**************************/
			
			map.put(TS[0], new Cost(1.88, 86, 22.48, 32, 269));
			map.put(TS[1], new Cost(1.96, 87, 19.40, 29, 264));
			map.put(TS[2], new Cost(2.11, 86, 21.48, 32, 270));
			map.put(TS[3], new Cost(4.99, 96, 23.68, 36, 285));
			map.put(TS[4], new Cost(1.91, 84, 21.60, 30, 264));
		}
	}
}
