package com.devsuperior.dsmovie.controllers;

import io.restassured.http.ContentType;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class MovieControllerRA {

    private Integer exitingMovieId;
    private String exitingMovieTitle;

    @BeforeEach
    void setUp() throws Exception {
        baseURI = "http://localhost:8080";

        exitingMovieId = 1;
        exitingMovieTitle = "The Witcher";
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
    public void findAllShouldReturnPagedMoviesWhenMovieTitleParamIsNotEmpty() {
        given()
                .header("Content-Type", "application-json")
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .param("title", exitingMovieTitle)
                .when()
                .get("/movies")
                .then()
                .statusCode(200)
                .body("content.id[0]", is(1))
                .body("content.title[0]", equalTo(exitingMovieTitle))
                .body("content.score[0]", is(4.5F))
                .body("content.count[0]", is(2))
                .body("content.image[0]", equalTo("https://www.themoviedb.org/t/p/w533_and_h300_bestv2/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg"))
        ;
    }

    @Test
    public void findByIdShouldReturnMovieWhenIdExists() {
        given()
                .header("Content-Type", "application-json")
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .get("/movies/{id}", exitingMovieId)
                .then()
                .statusCode(200)
                .body("id", is(1))
                .body("title", equalTo(exitingMovieTitle))
                .body("score", is(4.5F))
                .body("count", is(2))
                .body("image", equalTo("https://www.themoviedb.org/t/p/w533_and_h300_bestv2/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg"))
        ;
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
