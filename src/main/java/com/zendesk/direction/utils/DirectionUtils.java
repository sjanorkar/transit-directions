package com.zendesk.direction.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.zendesk.direction.entity.MrtStation;

/**
 * Utility methods class
 * @author swapnil.janorkar
 *
 */
public class DirectionUtils {

	/**
	 * private constructor
	 */
	private DirectionUtils() {

	}

	/**
	 * Check if Source and destination stations are on same mrt line
	 * @param from: source mrt station
	 * @param to: destination mrt station
	 * @return true if source and destination stations are on same line else false
	 */
	public static boolean isSameLine(List<MrtStation> from, List<MrtStation> to) {
		for (MrtStation f : from) {
			for (MrtStation t : to) {
				if (f.getLine().equals(t.getLine())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns index on mrt station on mrt line
	 * @param stations: all mrt stations on line
	 * @param station: station to find index
	 * @return index of station
	 */
	public static int getIndex(List<MrtStation> stations, String station) {
		int index = 0;
		for (MrtStation st : stations) {
			if (st.getName().equals(station)) {
				return index;
			}
			index++;
		}
		return -1;
	}

	/**
	 * Calculate total travel time between source and destination stations
	 * @param stations: List of mrt stations to travel
	 * @param dateTime: Date & time to start journey
	 * @return total travel time
	 */
	public static int calculateTravelTime(List<MrtStation> stations, LocalDateTime dateTime) {
		int time = 0;
		for (int i = 1; i < stations.size(); i++) {
			time += getTravelTime(stations.get(i - 1), stations.get(i), dateTime);
		}
		
		return time;
	}

	/**
	 * Generates travel summary 
	 * @param dateTime: date & time to start travel
	 * @return travel summary
	 */
	public static String getTravelSummery(LocalDateTime dateTime) {
		StringBuilder summery = new StringBuilder();
		if (isPeakHour(dateTime)) {
			summery.append("You're travelling during peak time expect crowded trains and some delay");
		} else if (isNightHour(dateTime)) {
			summery.append("You're travelling during night hours be alert and report to authorities in case of any trouble");
		}
		
		return summery.toString();
	}

	/**
	 * calculate arrival time at destination station
	 * @param datetime: date and time to start journey
	 * @param travelTime: time required to travel from source to destination
	 * @return arrival time at destination
	 */
	public static String calculateArrivalTime(LocalDateTime datetime, long travelTime) {
		LocalDateTime arrival = datetime.plusMinutes(travelTime);
		return arrival.format(DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT_STR));
	}
	
	/**
	 * Calculate total stations to travel 
	 * @param stations: list of mrt stations
	 * @return: total stations to travel
	 */
	public static int calculateStationsToTravel(List<MrtStation> stations) {
		Set<String> set = new HashSet<>();
		for (MrtStation st : stations) {
			set.add(st.getName());
		}
		
		return set.size() - 1;
	}
	
	/**
	 * Get travel time between two stations based on day and time
	 * @param source: source station
	 * @param destination: destination station
	 * @param dateTime: date & time to start journey
	 * @return time required travel between given stations
	 */
	public static int getTravelTime(MrtStation source, MrtStation destination, LocalDateTime dateTime) {
		int time  = 0;
		
		if (isPeakHour(dateTime)) {
			if ((source.getLine().equals(Constants.NORTH_SOUTH) && destination.getLine().equals(Constants.NORTH_SOUTH)) 
					||(source.getLine().equals(Constants.NORTH_EAST) && destination.getLine().equals(Constants.NORTH_EAST))) {
				time = 12;
			} else if (source.getLine().equals(destination.getLine())) {
				time = 10;
			} else {
				time = 15;
			}
		} else if (isNightHour(dateTime)) {
			if (source.getLine().equals(destination.getLine())) {
				if (source.getLine().equals(Constants.THOMSON)) {
					time = 8;
				} else {
					time = 10;
				}
			} else {
				time = 10;
			}
		} else {
			if (source.getLine().equals(destination.getLine())) {
				if (source.getLine().equals(Constants.DOWN_TOWN) 
						|| source.getLine().equals(Constants.THOMSON)) {
					time = 8;
				} else {
					time = 10;
				}
			} else {
				time = 10;
			}
		}
		
		return time;
	}

	public static boolean isPeakHour(LocalDateTime time) {
		boolean peakHour = false;
		
		if (time.getDayOfWeek().getValue() >= 1 && time.getDayOfWeek().getValue() <= 5) {
			if ((time.getHour() >= 6 && time.getHour() <= 9) || (time.getHour() >= 18 && time.getHour() <= 21)) {
				peakHour = true;
			}
		}

		return peakHour;
	}

	public static boolean isNightHour(LocalDateTime time) {
		boolean nightHour = false;
		
		if (time.getHour() >= 22 || time.getHour() <= 6) {
			nightHour = true;
		}

		return nightHour;
	}
}
