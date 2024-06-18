package com.devsuperior.dsmovie.controllers;

import com.devsuperior.dsmovie.tests.TokenUtil;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ScoreControllerRA {

	private String adminToken, adminUserName, adminPassword;

	private Integer exitingMovieId, nonExistingMovieID;
	private Map<String, Object> newScore;

	@BeforeEach
	void setUp() throws Exception {
		baseURI = "http://localhost:8080";

		// Admin
		adminUserName = "maria@gmail.com";
		adminPassword = "123456";
		adminToken = TokenUtil.obtainAccessToken(adminUserName, adminPassword);

		// Movie
		exitingMovieId = 1;
		nonExistingMovieID = 100;

		newScore = new HashMap<>();
		newScore.put("movieId", 1);
		newScore.put("score", 2.0F);
	}
	
	@Test
	public void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() throws Exception {
		newScore.put("movieId", nonExistingMovieID);
		JSONObject jsonBody = new JSONObject(newScore);
		given()
				.header("Content-Type", "application-json")
				.header("Authorization", "Bearer " + adminToken)
				.accept(ContentType.JSON)
				.contentType(ContentType.JSON)
				.body(jsonBody)
				.when()
				.put("/scores")
				.then()
				.statusCode(404)
				.body("error", equalTo("Recurso não encontrado"))
		;
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() throws Exception {
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() throws Exception {		
	}
}
