package guru.springframework.spring6reactive.repositories;

import guru.springframework.spring6reactive.config.DatabaseConfig;
import guru.springframework.spring6reactive.domain.Beer;
import guru.springframework.spring6reactive.domain.BeerStyle;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;

@DataR2dbcTest
@Import(DatabaseConfig.class)
class BeerRepositoryTest {

  @Autowired
  BeerRepository beerRepository;

  @Test
  void saveBeer() {
    beerRepository.save(getTestBeer())
        .subscribe(System.out::println);
  }

  @Test
  void createBeer() {

  }

  Beer getTestBeer() {
    return Beer.builder()
        .beerName("Space Dust")
        .beerStyle(BeerStyle.PILSNER.name())
        .upc("123213")
        .quantityOnHand(12)
        .price(BigDecimal.TEN)
        .build();
  }
}