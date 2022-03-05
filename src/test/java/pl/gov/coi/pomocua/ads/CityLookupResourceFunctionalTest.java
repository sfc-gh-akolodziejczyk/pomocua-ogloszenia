package pl.gov.coi.pomocua.ads;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.Collections;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CityLookupResourceFunctionalTest {

  @LocalServerPort
  int port;

  private static final String URL = "/api/dictionaries/city";

  @BeforeEach
  public void setUp() {
    RestAssured.port = port;
  }

  @ParameterizedTest
  @ValueSource(strings = {"Dzierżoniów", "dzierżoniów", "DZIERŻONIÓW", "Dzierż"})
  void shouldReturnMatchingCity(String query) {
    given()
        .queryParam("query", query)
        .when()
        .get(URL)
        .then()
        .assertThat()
        .statusCode(200)
        .body("cities[0].city", equalTo("dzierżoniów"))
        .body("cities[0].voivodeship", equalTo("dolnośląskie"));
  }

  @Test
  void shouldReturnListOfFiveMatchingCities() {
    given()
        .queryParam("query", "rud")
        .when()
        .get(URL)
        .then()
        .assertThat()
        .statusCode(200)
        .body("cities", hasSize(5))
        .body("cities[0].city", equalTo("ruda-huta"))
        .body("cities[0].voivodeship", equalTo("lubelskie"))
        .body("cities[1].city", equalTo("ruda maleniecka"))
        .body("cities[1].voivodeship", equalTo("świętokrzyskie"))
        .body("cities[2].city", equalTo("ruda śląska"))
        .body("cities[2].voivodeship", equalTo("śląskie"))
        .body("cities[3].city", equalTo("rudka"))
        .body("cities[3].voivodeship", equalTo("podlaskie"))
        .body("cities[4].city", equalTo("rudna"))
        .body("cities[4].voivodeship", equalTo("dolnośląskie"));
  }

  @ParameterizedTest
  @ValueSource(strings = {"abc", "123", "!@#"})
  void shouldReturnEmptyResponseWhenNoCitiesWereMatched(String query) {
    given()
        .queryParam("query", query)
        .when()
        .get(URL)
        .then()
        .assertThat()
        .statusCode(200)
        .body("cities", equalTo(Collections.EMPTY_LIST));
  }

  @ParameterizedTest
  @ValueSource(strings = {"", " ", "x", "xx"})
  @NullSource
  void shouldReturnNoContentForTooShortQuery(String query) {
    given()
        .queryParam("query", query)
        .when()
        .get(URL)
        .then()
        .assertThat()
        .statusCode(204);
  }

  @Test
  void shouldReturnBadRequestWhenNoQueryParam() {
    given()
        .when()
        .get(URL)
        .then()
        .assertThat()
        .statusCode(400);
  }
}
