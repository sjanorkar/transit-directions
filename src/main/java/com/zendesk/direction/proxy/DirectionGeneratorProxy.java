package com.zendesk.direction.proxy;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zendesk.direction.entity.MrtStation;
import com.zendesk.direction.service.DirectionsGeneratorService;
import com.zendesk.direction.utils.DirectionUtils;

/**
 * Service class for route generation
 * @author swapnil.janorkar
 *
 */
@Service
public class DirectionGeneratorProxy {

	private static final Logger LOGGER = LoggerFactory.getLogger(DirectionGeneratorProxy.class);
	
	@Autowired
	private DirectionsGeneratorService directionGenerator;
	
	/**
	 * Returns list of stations to travel for given source and destination mrt stations
	 * @param from: source mrt station 
	 * @param to: destination mrt station
	 * @param datetime: date & time to start journey
	 * @return Instructions to travel from source to destination station
	 */
	public String buildDirections(String from, String to, LocalDateTime datetime) {
		List<MrtStation> stations = directionGenerator.buildDirections(from, to);
		return generateInstructions(stations, datetime);
	}
	
	/**
	 * Generates instruction for traveling from source to destination mrt stations for give date & time
	 * @param mrtStations : list of mrt stations to travel
	 * @param dateTime: date & time to start journey
	 * @return: instructions for traveling from source to destination
	 */
	private String generateInstructions(List<MrtStation> mrtStations, LocalDateTime dateTime) {
		LOGGER.debug("Generating travel instructions");
		
		final MrtStation first = mrtStations.get(0);
		final MrtStation last = mrtStations.get(mrtStations.size() - 1);
		final String firstName = WordUtils.capitalizeFully(first.getName());
		final String lastName = WordUtils.capitalizeFully(last.getName());
		final long travelTime = DirectionUtils.calculateTravelTime(mrtStations, dateTime);
		final String arrivalTime = DirectionUtils.calculateArrivalTime(dateTime, travelTime);
		
		StringBuilder builder = new StringBuilder("Travel plan from " + firstName + "(" +first.getId() + ") to " + lastName + "(" + last.getId() + ")");
		builder.append(System.lineSeparator());
		builder.append(DirectionUtils.getTravelSummery(dateTime));
		builder.append(DirectionUtils.getTravelSummery(dateTime).isEmpty() ? "" : System.lineSeparator());
		builder.append("Total stations to travel: " + DirectionUtils.calculateStationsToTravel(mrtStations));
		builder.append(System.lineSeparator());
		builder.append("Total travel time: " + travelTime + " mins");
		builder.append(System.lineSeparator());
		builder.append("Expected arrival time at " + lastName + "(" + last.getId() + ") " + arrivalTime);
		builder.append(System.lineSeparator() + System.lineSeparator());
		builder.append("Board " + first.getLineName() + " line at " + firstName + "(" + first.getId() + ")");
		builder.append(System.lineSeparator());

		for (int i = 1; i < mrtStations.size(); i++) {
			final MrtStation prev = mrtStations.get(i - 1);
			final MrtStation curr = mrtStations.get(i);
			final String prevName = WordUtils.capitalizeFully(prev.getName());
			final String currName = WordUtils.capitalizeFully(curr.getName());
			
			if (prev.getLine().equals(curr.getLine())) {
				builder.append("Take " + prev.getLineName() + " line from " + prevName  + "(" + prev.getId() + ") to " + currName + "(" + curr.getId() + ")");
				builder.append(System.lineSeparator());
			} else {
				builder.append("Change from " + prev.getLineName() + " line to " + curr.getLineName() + " line at " + prevName);
				builder.append(System.lineSeparator());
			}
		}
		builder.append("Alight " + last.getLineName() + " line at " + lastName + "(" + last.getId() + ")");
		return builder.toString();
	}
}
