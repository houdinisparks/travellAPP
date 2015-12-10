package com.example.zhenyang.app;

public class Variables {

	// Variables can be changed with user input in Android
	private static double travelBudget;
	private static double timeConstraint = 1000;

	public static void setBudget(double budget)
	{
		travelBudget = budget;
	}
	public static double getBudget()
	{
		return travelBudget;
	}
	
}
