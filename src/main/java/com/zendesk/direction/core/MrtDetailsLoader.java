package com.zendesk.direction.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.zendesk.direction.entity.MrtStation;

/**
 * Reads mrt station details from csv and generates appropriate data structure
 * @author swapnil.janorkar
 *
 */
@Service
public class MrtDetailsLoader {

	private static final Logger LOGGER = LoggerFactory.getLogger(MrtDetailsLoader.class);
	
	private List<MrtStation> mrtStations;
	
	// lines
	private Map<String, List<MrtStation>> mrtLines = new HashMap<>();
	//stationLines
	private Map<MrtStation, List<String>> stationLines = new HashMap<>();
	private Map<String, Set<String>> linesInterchange = new HashMap<>();
	private Map<String, List<MrtStation>> stations = new HashMap<>();
	private Map<String, String> mrtIdNameMap = new HashMap<>();
	
	/**
	 * Reads mrt station information from csv file
	 * @throws IOException in case station info file is not present
	 */
	@PostConstruct
	public void init() throws IOException {
		LOGGER.debug("loading station_map.csv");
		try (InputStream inputStream = getClass().getResourceAsStream("/station_map.csv");
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

			CsvToBean<MrtStation> csvToBean = new CsvToBeanBuilder<MrtStation>(reader)
					.withType(MrtStation.class)
					.withIgnoreLeadingWhiteSpace(true)
					.withSkipLines(1)
					.build();

			this.mrtStations = csvToBean.parse();
		}
		for (MrtStation mrt : mrtStations) {
			buildMrtLine(mrt);
			associateStationToLine(mrt);
			associateNameToStation(mrt.getName(), mrt);
			mrtIdNameMap.put(mrt.getId(), mrt.getName());
		}
		
		buildLinesInterchangeMap();
		LOGGER.debug("loaded station_map.csv successfuly");
	}
	
	/**
	 * Station and interchange lines map
	 */
	private void buildLinesInterchangeMap() {
		LOGGER.debug("Associating MRT station with MRT lines");
		
		for (Entry<MrtStation, List<String>> entry : stationLines.entrySet()) {
			if (linesInterchange.containsKey(entry.getKey().getLine())) {
				linesInterchange.get(entry.getKey().getLine()).addAll(entry.getValue());
			} else {
				Set<String> tempSet = new HashSet<>(entry.getValue());
				linesInterchange.put(entry.getKey().getLine(), tempSet);
			}
		}

		for (Entry<String, Set<String>> entry : linesInterchange.entrySet()) {
			for (String str : entry.getValue()) {
				linesInterchange.get(str).add(entry.getKey());
			}
		}

		for (Entry<String, Set<String>> entry : linesInterchange.entrySet()) {
			entry.getValue().remove(entry.getKey());
		}
	}
	
	/**
	 * Associate given station name to appropriate MrtStation object
	 * @param stationStr: station name in string format
	 * @param station: MrtStation object
	 */
	private void associateNameToStation(String stationStr, MrtStation station) {
		LOGGER.debug("Associating MRT stations names with MRT station object");
		
		if (stations.containsKey(stationStr)) {
			stations.get(stationStr).add(station);
		} else {
			List<MrtStation> list = new ArrayList<>();
			list.add(station);
			stations.put(stationStr, list);
		}
	}
	
	/**
	 * Associate MrtStation to appropriate line
	 * @param station: mrt station 
	 */
	private void buildMrtLine(MrtStation station) {
		LOGGER.debug("Associating MRT stations with MRT lines");
		
		if (mrtLines.containsKey(station.getLine())) {
			mrtLines.get(station.getLine()).add(station);
		} else {
			List<MrtStation> tempStations = new ArrayList<>();
			tempStations.add(station);
			mrtLines.put(station.getLine(), tempStations);
		}
	}
	
	/**
	 * Associate MrtStation appropriate lines.
	 * Some of the mrt stations belongs to multiple line, this data structure keeps track of that
	 * @param station MrtStation
	 */
	private void associateStationToLine(MrtStation station) {
		LOGGER.debug("Associating MRT stations with MRT lines");
		
		if (stationLines.containsKey(station)) {
			List<String> tempLines = stationLines.get(station);
			tempLines.add(station.getLine());
			stationLines.put(station, tempLines);

		} else {
			List<String> tempLines = new ArrayList<>();
			tempLines.add(station.getLine());
			stationLines.put(station, tempLines);
		}
	}

	/**
	 * Returns list of MrtStation
	 * @return list of MrtStation
	 */
	public List<MrtStation> getMrtStations() {
		return this.mrtStations;
	}

	/**
	 * Returns mrt line and associated stations
	 * @return mrt line and associated stations
	 */
	public Map<String, List<MrtStation>> getMrtLines() {
		return mrtLines;
	}

	/**
	 * Returns MrtStation and associates lines map
	 * @return MrtStation and associates lines map
	 */
	public Map<MrtStation, List<String>> getStationLines() {
		return stationLines;
	}

	/**
	 * Returns line interchange map
	 * @return line interchange map
	 */
	public Map<String, Set<String>> getLinesInterchange() {
		return linesInterchange;
	}

	/**
	 * Returns map of station name to MrtStation map
	 * @return map of station name to MrtStation map
	 */
	public Map<String, List<MrtStation>> getStations() {
		return stations;
	}
	
	/**
	 * Return mrt name from id
	 * @param id: mrt id
	 * @return mrt name
	 */
	public String getMrtName(String id) {
		return mrtIdNameMap.get(id);
	}
}
