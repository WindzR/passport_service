package ru.job4j.controller;

import io.restassured.http.ContentType;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ru.job4j.domain.Passport;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

class PassportControllerTest {

    private String URL = "http://localhost:8080/";

    @Autowired
    PassportController controller;

    @Test
    void whenFindAllPassports() {
        List<Passport> passports = given()
                .when()
                .contentType(ContentType.JSON)
                .get(URL + "find")
                .then().log().all()
                .statusCode(200)
                .extract().body().jsonPath().getList("", Passport.class);
        assertEquals(4, passports.size());
        passports.forEach(x -> {
            assertTrue(x.getId() != 0);
            assertNotNull(x.getSeries());
            assertNotNull(x.getExpiredDate());
        });
    }

    @Test
    void whenFindPassportBySeries() {
        Passport passport = given()
                .when()
                .contentType(ContentType.JSON)
                .get(URL + "find-series?seria=AA0002RU")
                .then().log().all()
                .statusCode(200)
                .extract().body().jsonPath().getObject("", Passport.class);
        assertEquals("AA0002RU", passport.getSeries());
    }

    @Test
    void whenFindUnavailablePassports() {
        List<Passport> passports = given()
                .when()
                .contentType(ContentType.JSON)
                .get(URL + "unavailable")
                .then().log().all()
                .statusCode(200)
                .extract().body().jsonPath().getList("", Passport.class);
        assertEquals(1, passports.size());
        assertEquals("AB1234RU", passports.get(0).getSeries());
    }

    @Test
    void whenFindExpirationDatePassports() {
        List<Passport> passports = given()
                .when()
                .contentType(ContentType.JSON)
                .get(URL + "find-replaceable")
                .then().log().all()
                .statusCode(200)
                .extract().body().jsonPath().getList("", Passport.class);
        assertEquals(1, passports.size());
        assertEquals("AX4321RU", passports.get(0).getSeries());
    }
}