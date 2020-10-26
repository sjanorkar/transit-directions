package com.zendesk.direction.utils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

/**
 * Constant interface
 * @author swapnil.janorkar
 *
 */
public interface Constants {
	
	String DATE_TIME_FORMAT_STR = "dd/MMM/yyyy hh:mm a";
	String DATE_FORMAT_STR = "dd MMMM yyyy";
	SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_STR);
	List<String> MRT_LINES = Arrays.asList( "NS", "EW", "CG", "NE", "CC", "DT",  "TE");
	String THOMSON = "TE";
	String DOWN_TOWN = "DT";
	String EAST_WEST = "EW";
	String NORTH_EAST = "NE";
	String CIRCLE = "CC";
	String NORTH_SOUTH = "NS";
	String CHANGI_GREEN = "CG";
	String CIRCLE_EXTENSION = "CE";
	
	String THOMSON_NAME = "Thomson";
	String DOWN_TOWN_NAME = "Down Town";
	String EAST_WEST_NAME = "East West";
	String NORTH_EAST_NAME = "North East";
	String CIRCLE_NAME = "Circle";
	String NORTH_SOUTH_NAME = "North South";
	String CHANGI_GREEN_NAME = "Changi Green";
	String CIRCLE_EXTENSION_NAME = "Cirlce Extension";
	
}
