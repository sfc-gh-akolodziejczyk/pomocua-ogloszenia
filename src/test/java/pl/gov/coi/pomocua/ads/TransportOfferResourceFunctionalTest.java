package pl.gov.coi.pomocua.ads;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import pl.gov.coi.pomocua.ads.transport.TransportOffer;
import pl.gov.coi.pomocua.ads.transport.TransportOfferRepository;

import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransportOfferResourceFunctionalTest {

  @Autowired
  TransportOfferRepository transportOfferRepository;

  @LocalServerPort
  int port;

  private static final String POST_URL = "/api/secure/transport";
  private static final String GET_URL = "/api/transport";

  @BeforeEach
  public void setUp() {
    RestAssured.port = port;
    transportOfferRepository.deleteAll();
  }

  @Test
  void shouldAddTransferOffer() {
    //when
    ValidatableResponse response = postTransferOffer();
    //then
    assertResponseBody(response);
    //and
    Optional<TransportOffer> transportOfferOptional = transportOfferRepository.findById(1L);
    assertThat(transportOfferOptional.isPresent()).isTrue();
    TransportOffer transportOffer = transportOfferOptional.get();
    assertThat(transportOffer.id).isEqualTo(1L);
    assertThat(transportOffer.title).isEqualTo("testTitle");
    assertThat(transportOffer.description).isEqualTo("testDescription");
    assertThat(transportOffer.origin.voivodeship).isEqualTo("Małopolska");
    assertThat(transportOffer.origin.city).isEqualTo("Kraków");
    assertThat(transportOffer.destination.voivodeship).isEqualTo("Mazowieckie");
    assertThat(transportOffer.destination.city).isEqualTo("Warszawa");
    assertThat(transportOffer.capacity).isEqualTo(3);
    assertThat(transportOffer.transportDate).isEqualTo("2022-03-05");
  }

  @Test
  void shouldGetTransferOfferById() {
    //given
    postTransferOffer();
    //when
    ValidatableResponse response = given()
        .when()
        .get(GET_URL + "/1")
        .then()
        .assertThat()
        .statusCode(200);
    //then
    assertResponseBody(response);
  }

  private ValidatableResponse postTransferOffer() {
    return given()
        .body(getBody())
        .contentType(ContentType.JSON)
        .when()
        .post(POST_URL)
        .then()
        .assertThat()
        .statusCode(201);
  }

  private String getBody(Long id) {
    return """
            {
              "id": 1,
              "title": "testTitle",
              "description": "testDescription",
              "origin": {
                "voivodeship": "Małopolska",
                "city": "Kraków"
              },
              "destination": {
                "voivodeship": "Mazowieckie",
                "city": "Warszawa"
              },
              "capacity": 3,
              "transportDate": "2022-03-05"
            }
        """;
  }

  private void assertResponseBody(ValidatableResponse response) {
    response
        .body("id", equalTo(1))
        .body("title", equalTo("testTitle"))
        .body("description", equalTo("testDescription"))
        .body("origin.voivodeship", equalTo("Małopolska"))
        .body("origin.city", equalTo("Kraków"))
        .body("destination.voivodeship", equalTo("Mazowieckie"))
        .body("destination.city", equalTo("Warszawa"))
        .body("capacity", equalTo(3))
        .body("transportDate", equalTo("2022-03-05"));
  }
}
