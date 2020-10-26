package com.zendesk.direction.controller;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.zendesk.direction.core.MrtDetailsLoader;
import com.zendesk.direction.entity.MrtStation;
import com.zendesk.direction.exception.StationClosedException;
import com.zendesk.direction.exception.StationNotFoundException;
import com.zendesk.direction.exception.StationNotReadyException;
import com.zendesk.direction.utils.Constants;

/**
 * Base controller for RouteController
 * @author swapnil.janorkar
 *
 */
@Controller
public class BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);
	
	@Autowired
	private MrtDetailsLoader mrtDetails;
	
	/**
	 * Check if mrt station is valid or not
	 * @param id : mrt station id
	 * @throws StationNotFoundException in case mrt station id is invalid
	 */
	protected void validateStationId(String id) throws StationNotFoundException {
		if (mrtDetails.getMrtName(id) == null) {
			throw new StationNotFoundException("Station id " + id);
		}
	}
	
	/**
	 * Validate the user input
	 * @param from : source mrt station
	 * @param to: destination mrt station
	 * @param dateTime: Date and time of travel
	 * @throws StationNotFoundException : if station is not present
	 * @throws ParseException: if Datetime is invalid
	 * @throws StationClosedException: if station is closed at night
	 * @throws StationNotReadyException: if station is not ready yet
	 */
	protected void validateInput(String from, String to, LocalDateTime dateTime) throws StationNotFoundException, ParseException, StationClosedException, StationNotReadyException {
		if (!isStationExist(from)) {
			throw new StationNotFoundException("Station name " + from);
		}
		
		if (!isStationExist(to)) {
			throw new StationNotFoundException("Station name " + to);
		}
		
		if (!isReady(from, dateTime)) {
			throw new StationNotReadyException("Station name " + from);
		}
		
		if (!isReady(to, dateTime)) {
			throw new StationNotReadyException("Station name " + to);
		}
		
		if (isClose(from, dateTime)) {
			throw new StationClosedException("Station name " + from);
		}
		
		if (isClose(to, dateTime)) {
			throw new StationClosedException("Station name " + to);
		}
	}
	
	/**
	 * Get mrt station name for given station id
	 * @param id: mrt station id
	 * @return Associated mrt station name
	 */
	protected String getMrtName(String id) {
		return mrtDetails.getMrtName(id);
	}
	
	/**
	 * check if given mrt station exists
	 * @param station: mrt station name
	 * @return true if mrt station is present else false
	 */
	private boolean isStationExist(String station) {
		boolean exist = true;
		Map<String, List<MrtStation>> stations = mrtDetails.getStations();

		if (!stations.containsKey(station) && mrtDetails.getMrtName(station) == null) {
			exist = false;
		}

		return exist;
	}

	/**
	 * Check if mrt station is ready
	 * @param station: mrt station name
	 * @param dateTimes: date time to start journey
	 * @return true if station is ready else false
	 * @throws ParseException: if date time is invalid
	 */
	private boolean isReady(String station, LocalDateTime dateTimes) throws ParseException {
		boolean ready = true;
		Map<String, List<MrtStation>> stations = mrtDetails.getStations();
		final Date date = Date.from(dateTimes.atZone(ZoneId.systemDefault()).toInstant());
		
		for (MrtStation st : stations.get(station)) {
			if (st.getDate().compareTo(date) == 1) {
				ready = false;
			} else {
				ready = true;
			}
		}

		return ready;
	}
	
	/**
	 * Check if mrt station is closed 
	 * @param station: mrt station name
	 * @param dateTime: date time to start journey
	 * @return true if station is closed else false
	 */
	private boolean isClose(String station, LocalDateTime dateTime) {
		boolean close = true;
		Map<String, List<MrtStation>> stations = mrtDetails.getStations();

		for (MrtStation st : stations.get(station)) {
			if (st.getLine().equals(Constants.CHANGI_GREEN) 
					|| st.getLine().equals(Constants.DOWN_TOWN) 
					|| st.getLine().equals(Constants.CIRCLE_EXTENSION)) {
				if (dateTime.getHour() >= 22 || dateTime.getHour() < 6) {
					close = close && Boolean.TRUE;
				} else {
					close = close && Boolean.FALSE;
				}
			} else {
				close = close && Boolean.FALSE;
			}
		}

		return close;
	}
}
