package me.invertmc.sql;

public class SQLCount {

	private static long queries = 0;
	
	public static void count() {
		queries++;
	}
	
	public static long getTotalQueries(){
		return queries;
	}
	
}
