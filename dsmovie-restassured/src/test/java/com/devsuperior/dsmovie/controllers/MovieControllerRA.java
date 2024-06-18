package com.devsuperior.dsmovie.controllers;

import com.devsuperior.dsmovie.tests.TokenUtil;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class MovieControllerRA {

    private Long newMovieId;
    private Integer exitingMovieId, nonExistingMovieID, countMovie;
    private String exitingMovieTitle, newMovieTitle, newMovieImage;
    private Double scoreMovie;

    private Map<String, Object> postMovie;
    private String adminToken, adminUserName, adminPassword;
    private String clientToken, clientUserName, clientPassword, invalidToken;

    @BeforeEach
    void setUp() throws Exception {
        baseURI = "http://localhost:8080";

        // Admin
        adminUserName = "maria@gmail.com";
        adminPassword = "123456";
        adminToken = TokenUtil.obtainAccessToken(adminUserName, adminPassword);

        // Cliente
        clientUserName = "alex@gmail.com";
        clientPassword = "123456";
        clientToken = TokenUtil.obtainAccessToken(clientUserName, clientPassword);

        exitingMovieId = 1;
        nonExistingMovieID = 200;
        exitingMovieTitle = "The Witcher";

        // Novo Movie
        newMovieTitle = "Home Aranha";
        scoreMovie = 0.0;
        countMovie = 0;
        newMovieImage = "new_image.jpg";

        postMovie = new HashMap<>();
        postMovie.put("title", newMovieTitle);
        postMovie.put("score", scoreMovie);
        postMovie.put("count", countMovie);
        postMovie.put("image", newMovieImage);

        invalidToken = adminToken+"eld";
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
        given()
                .header("Content-Type", "application-json")
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .get("/movies/{id}", nonExistingMovieID)
                .then()
                .statusCode(404)
                .body("error", equalTo("Recurso não encontrado"))
        ;

    }

    @Test
    public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndBlankTitle() throws JSONException {

        postMovie.put("title", " ");
        JSONObject newMovie = new JSONObject(postMovie);

        given()
                .header("Content-Type", "application-json")
                .header("Authorization", "Bearer " + adminToken)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(newMovie)
                .when()
                .post("/movies")
                .then()
                .statusCode(422)
                .body("error", equalTo("Dados inválidos"))
        ;
    }

    @Test
    public void insertShouldReturnForbiddenWhenClientLogged() throws Exception {
        JSONObject newMovie = new JSONObject(postMovie);

        given()
                .header("Content-Type", "application-json")
                .header("Authorization", "Bearer " + clientToken)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(newMovie)
                .when()
                .post("/movies")
                .then()
                .statusCode(403)
        ;
    }

    @Test
    public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
        JSONObject newMovie = new JSONObject(postMovie);

        given()
                .header("Content-Type", "application-json")
                .header("Authorization", "Bearer " + invalidToken)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(newMovie)
                .when()
                .post("/movies")
                .then()
                .statusCode(401)
        ;
    }
}
