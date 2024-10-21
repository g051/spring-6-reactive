package guru.springframework.spring6reactive.controllers;

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

  @Test
  @Order(2)
  void listBeers() {
    webTestClient.get()
        .uri(BeerController.BEER_PATH)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().valueEquals("Content-Type", "application/json")
        .expectBody().jsonPath("$.size()").isEqualTo(3);
  }

  @Test
  @Order(1)
  void getBeerById() {
    webTestClient.get()
        .uri(BeerController.BEER_PATH_ID, 1)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType("application/json")
        .expectBody(BeerDTO.class);
  }

  @Test
  void createBeer() {
    webTestClient.post()
        .uri(BeerController.BEER_PATH)
        .body(Mono.just(BeerRepositoryTest.getTestBeer()), BeerDTO.class)
        .header("Content-Type", "application/json")
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().location(BeerController.BEER_PATH + "/4");
  }

  @Test
  @Order(3)
  void updateBeer() {
    webTestClient.put()
        .uri(BeerController.BEER_PATH_ID, 1)
        .body(Mono.just(BeerRepositoryTest.getTestBeer()), BeerDTO.class)
        .exchange()
        .expectStatus().isNoContent();
  }

  @Test
  @Order(4)
  void patchBeer() {
    webTestClient.patch()
        .uri(BeerController.BEER_PATH_ID, 2)
        .body(Mono.just(BeerRepositoryTest.getTestBeer()), BeerDTO.class)
        .exchange()
        .expectStatus().isNoContent();
  }

  @Test
  @Order(99)
  void deleteBeer() {
    webTestClient.delete()
        .uri(BeerController.BEER_PATH_ID, 1)
        .exchange()
        .expectStatus().isNoContent();
  }
}