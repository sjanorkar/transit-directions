package com.zendesk.direction.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global exception handler
 * @author swapnil.janorkar
 *
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);
	
	/**
	 * Catches datetime parse exception
	 * @return http response 
	 */
   @ExceptionHandler(MethodArgumentTypeMismatchException.class)
   protected ResponseEntity<String> incorrectDateFormat() {
	   LOGGER.error("Cannot parse input datetime, Datetime must be in format dd-MM-yyyy HH:mm");
	   return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datetime must be in format dd-MM-yyyy HH:mm"); 
   }
   
   /**
    * Catches any un-handled exception
    * @return http response
    */
   @ExceptionHandler(Exception.class)
   protected ResponseEntity<String> unhandledError() {
	   return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unknown error occured, please try again after sometime"); 
   }
}
