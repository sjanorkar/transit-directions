package com.zendesk.direction.entity;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

import com.opencsv.bean.CsvBindByPosition;
import com.zendesk.direction.utils.Constants;

/**
 * Entity class for Mrt station information.
 * station_map.csv fields are mapped to this class
 * @author swapnil.janorkar
 *
 */
public class MrtStation {

	@CsvBindByPosition(position = 0)
	private String id;
	
	@CsvBindByPosition(position = 1)
	private String name;
	
	@CsvBindByPosition(position = 2)
	private String date;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.toLowerCase();
	}

	public Date getDate() throws ParseException {
		return Constants.DATE_FORMAT.parse(date);
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getLine() {
		return id.substring(0, 2);
	}
	
	public String getLineName() {
		String lineName = "";
		
		switch(id.substring(0, 2)) {
			case Constants.CHANGI_GREEN:
				lineName = Constants.CHANGI_GREEN;
				break;
			case Constants.CIRCLE:
				lineName = Constants.CIRCLE_NAME;
				break;
			case Constants.DOWN_TOWN:
				lineName = Constants.DOWN_TOWN_NAME;
				break;
			case Constants.EAST_WEST:
				lineName = Constants.EAST_WEST_NAME;
				break;
			case Constants.NORTH_EAST:
				lineName = Constants.NORTH_EAST_NAME;
				break;
			case Constants.NORTH_SOUTH:
				lineName = Constants.NORTH_SOUTH_NAME;
				break;
			case Constants.THOMSON:
				lineName = Constants.THOMSON_NAME;
				break;
			default:
				lineName = "";
		}
		
		return lineName;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MrtStation other = (MrtStation) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return  id + ": " + name;
	}
}
