package com.zendesk.direction.controller;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zendesk.direction.entity.TravelPlan;
import com.zendesk.direction.proxy.DirectionGeneratorProxy;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;

/**
 * Direction endpoint serving controller
 * @author swapnil.janorkar
 *
 */
@OpenAPIDefinition(
	info=@Info (
		title="Singapore MRT travel helper",
		version="0.1",
		description="Travel planner and helper for Singapore MRT",
		contact=@Contact(name="Swapnil Janorkar", email="swapnil.janorkar@gmail.com")
	),
	servers= {
		@Server(
			description="Local developement server",
			url="http://localhost:8080"
		)
	}
)
@RestController
@RequestMapping("/directions")
public class DirectionController extends BaseController {

	@Autowired
	private DirectionGeneratorProxy routeGenerator;
	
	/**
	 * Generates directions for given source and destination mrt station name for current time
	 * @param from: source mrt station name
	 * @param to: destination mrt station name
	 * @return Travel plan between source and destination station
	 * @throws ParseException: if datetime is invalid
	 */
	@Operation(
		summary="Get travel plan for given station names",
		description="Get travel plan for source and destination station according to current time",
		responses = {
				@ApiResponse(responseCode="200", description="Route description between given MRT stations"),
				@ApiResponse(responseCode="400", description="Invalid station name/id, Station either closed or not started yet")
		}
	)
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/mrt/from/{from}/to/{to}", method=RequestMethod.GET)
	public ResponseEntity<TravelPlan> getRouteByStationName(
			@Parameter(description="Source MRT station name", required=true) @PathVariable("from") String from,
			@Parameter(description="Destination MRT station name", required=true) @PathVariable("to") String to) throws ParseException {
		
		final LocalDateTime dateTime = LocalDateTime.now();
		TravelPlan plan = new TravelPlan();
		try { 
			from = from.toLowerCase();
			to = to.toLowerCase();
			super.validateInput(from, to, dateTime);
		} catch(Exception exception){
			plan.setError(exception.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(plan);
		}

		plan = routeGenerator.buildDirections(from, to, dateTime);
		return ResponseEntity.ok().body(plan);
	}
	
	/**
	 * Generates directions for given source and destination mrt station name for current time
	 * @param from: source mrt station name
	 * @param to: destination mrt station name
	 * @param datetime: date & time to start journey
	 * @return Travel plan between source and destination station
	 * @throws ParseException: if datetime is invalid
	 */
	@Operation(
		summary="Get travel plan for given station names",
		description="Get travel plan for source and destination station according to given time",
		responses = {
				@ApiResponse(responseCode="200", description="Route description between given MRT stations"),
				@ApiResponse(responseCode="400", description="Invalid station name/id, Station either closed or not started yet")
		}
	)
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/mrt/from/{from}/to/{to}/datetime/{dateTime}", method=RequestMethod.GET)
	public ResponseEntity<TravelPlan> getRouteByStationName(
			@Parameter(description="Source MRT station name", required=true) @PathVariable("from") String from,
			@Parameter(description="Destination MRT station name", required=true) @PathVariable("to") String to, 
			@Parameter(description="Time to start journey", required=true) @PathVariable("dateTime") @DateTimeFormat(pattern="dd-MM-yyyy HH:mm") Date date) throws ParseException {
		
		final LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
		
		try { 
			from = from.toLowerCase();
			to = to.toLowerCase();
			super.validateInput(from, to, dateTime);
		} catch(Exception exception){
			TravelPlan plan = new TravelPlan();
			plan.setError(exception.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(plan);
		}
		
		TravelPlan plan = routeGenerator.buildDirections(from, to, dateTime);
		return ResponseEntity.ok().body(plan);
	}
	
	
	/**
	 * Generates directions for given source and destination mrt station ids for current time
	 * @param from: source mrt station id
	 * @param to: destination mrt station id
	 * @return Travel plan between source and destination station
	 * @throws ParseException: if datetime is invalid
	 */
	@Operation(
		summary="Get travel plan for given station ids",
		description="Get travel plan for source and destination station according to current time",
		responses = {
				@ApiResponse(responseCode="200", description="Route description between given MRT stations"),
				@ApiResponse(responseCode="400", description="Invalid station name/id, Station is either closed or not started yet")
		}
	)
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/id/from/{from}/to/{to}", method=RequestMethod.GET)
	public ResponseEntity<TravelPlan> getRouteByStationId(
			@Parameter(description="Source MRT station id", required=true) @PathVariable("from") String fromId,
			@Parameter(description="Destination MRT station id", required=true) @PathVariable("to") String toId) throws ParseException {
		
		final LocalDateTime dateTime = LocalDateTime.now();
		final String from = super.getMrtName(fromId);
		final String to = super.getMrtName(toId);
		
		try { 
			validateStationId(fromId);
			validateStationId(toId);
			validateInput(from, to, dateTime);
		} catch(Exception exception){
			TravelPlan plan = new TravelPlan();
			plan.setError(exception.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(plan);
		}

		TravelPlan plan = routeGenerator.buildDirections(from, to, dateTime);
		return ResponseEntity.ok().body(plan);
	}
	
	/**
	 * Generates directions for given source and destination mrt station ids for current time
	 * @param from: source mrt station id
	 * @param to: destination mrt station id
	 * @param dateTime: date & time to start journey
	 * @return Travel plan between source and destination station
	 * @throws ParseException: if datetime is invalid
	 */
	@Operation(
		summary="Get travel plan for given station ids",
		description="Get travel plan for source and destination station according to current time",
		responses = {
				@ApiResponse(responseCode="200", description="Route description between given MRT stations"),
				@ApiResponse(responseCode="400", description="Invalid station name/id, Station is either closed or not started yet")
		}
	)
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/id/from/{from}/to/{to}/datetime/{dateTime}", method=RequestMethod.GET)
	public ResponseEntity<TravelPlan> getRouteByStationId(
			@Parameter(description="Source MRT station id", required=true) @PathVariable("from") String fromId,
			@Parameter(description="Source MRT station id", required=true) @PathVariable("to") String toId, 
			@Parameter(description="Time to start journey", required=true) @PathVariable("dateTime") @DateTimeFormat(pattern="dd-mm-yyyy HH:mm") Date date) throws ParseException {
		
		final LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
		final String from = super.getMrtName(fromId);
		final String to = super.getMrtName(toId);
		
		try { 
			validateStationId(fromId);
			validateStationId(toId);
			validateInput(from, to, dateTime);
		} catch(Exception exception){
			TravelPlan plan = new TravelPlan();
			plan.setError(exception.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(plan);
		}

		TravelPlan plan = routeGenerator.buildDirections(from, to, dateTime);
		return ResponseEntity.ok().body(plan);
	}
}
