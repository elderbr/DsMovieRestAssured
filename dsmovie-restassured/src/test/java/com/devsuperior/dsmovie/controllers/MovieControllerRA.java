package com.devsuperior.dsmovie.controllers;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class MovieControllerRA {

	@BeforeEach
	void setUp() throws Exception {
		baseURI = "http://localhost:8080";
	}
	
	@Test
	public void findAllShouldReturnOkWhenMovieNoArgumentsGiven() throws Exception {
		given()
				.get("/movies")
				.then()
				.statusCode(200)
		;
	}
	
	@Test
	public void findAllShouldReturnPagedMoviesWhenMovieTitleParamIsNotEmpty(){
	}
	
	@Test
	public void findByIdShouldReturnMovieWhenIdExists(){
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() {
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndBlankTitle() throws JSONException {		
	}
	
	@Test
	public void insertShouldReturnForbiddenWhenClientLogged() throws Exception {
	}
	
	@Test
	public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
	}
}
