package guru.springframework.spring6reactive.controllers;

import guru.springframework.spring6reactive.domain.Beer;
import guru.springframework.spring6reactive.model.BeerDTO;
import guru.springframework.spring6reactive.repositories.BeerRepositoryTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BeerControllerTest {

  @Autowired
  WebTestClient webTestClient;

  Beer beer = BeerRepositoryTest.getTestBeer();
  
  @Test
  @Order(20)
  void listBeers() {
    webTestClient.get()
        .uri(BeerController.BEER_PATH)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().valueEquals("Content-Type", "application/json")
        .expectBody().jsonPath("$.size()").isEqualTo(3);
  }

  @Test
  @Order(10)
  void getBeerById() {
    webTestClient.get()
        .uri(BeerController.BEER_PATH_ID, 1)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType("application/json")
        .expectBody(BeerDTO.class);
  }

  @Test
  @Order(11)
  void getBeerByIdNotFound() {
    webTestClient.get()
        .uri(BeerController.BEER_PATH_ID, 999)
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  void createBeer() {
    webTestClient.post()
        .uri(BeerController.BEER_PATH)
        .body(Mono.just(beer), BeerDTO.class)
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().location(BeerController.BEER_PATH + "/4");
  }

  @Test
  void createBeerBadData() {
    Beer testBeer = BeerRepositoryTest.getTestBeer();
    testBeer.setBeerName("");

    webTestClient.post()
        .uri(BeerController.BEER_PATH)
        .body(Mono.just(testBeer), BeerDTO.class)
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  @Order(30)
  void updateBeer() {
    webTestClient.put()
        .uri(BeerController.BEER_PATH_ID, 1)
        .body(Mono.just(beer), BeerDTO.class)
        .exchange()
        .expectStatus().isNoContent();
  }

  @Test
  @Order(31)
  void updateBeerBadData() {
    Beer testBeer = BeerRepositoryTest.getTestBeer();
    testBeer.setBeerStyle("");

    webTestClient.put()
        .uri(BeerController.BEER_PATH_ID, 1)
        .body(Mono.just(testBeer), BeerDTO.class)
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void updateBeerNotFound() {
    webTestClient.put()
        .uri(BeerController.BEER_PATH_ID, 999)
        .body(Mono.just(beer), BeerDTO.class)
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  @Order(40)
  void patchBeer() {
    webTestClient.patch()
        .uri(BeerController.BEER_PATH_ID, 2)
        .body(Mono.just(beer), BeerDTO.class)
        .exchange()
        .expectStatus().isNoContent();
  }

  @Test
  void patchBeerNotFound() {
    webTestClient.patch()
        .uri(BeerController.BEER_PATH_ID, 999)
        .body(Mono.just(beer), BeerDTO.class)
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  @Order(99)
  void deleteBeer() {
    webTestClient.delete()
        .uri(BeerController.BEER_PATH_ID, 1)
        .exchange()
        .expectStatus().isNoContent();
  }

  @Test
  void deleteBeerNotFound() {
    webTestClient.delete()
        .uri(BeerController.BEER_PATH_ID, 999)
        .exchange()
        .expectStatus().isNotFound();
  }
}