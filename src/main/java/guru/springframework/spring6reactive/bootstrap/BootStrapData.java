package guru.springframework.spring6reactive.bootstrap;

import guru.springframework.spring6reactive.domain.Beer;
import guru.springframework.spring6reactive.domain.BeerStyle;
import guru.springframework.spring6reactive.domain.Customer;
import guru.springframework.spring6reactive.repositories.BeerRepository;
import guru.springframework.spring6reactive.repositories.CustomerRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BootStrapData implements CommandLineRunner {

  private final BeerRepository beerRepository;
  private final CustomerRepository customerRepository;

  @Override
  public void run(String... args) throws Exception {
    loadBeerData();
    beerRepository.count().subscribe(count -> System.out.println("Loaded beers: " + count));

    loadCustomerData();
    customerRepository.count().subscribe(count -> System.out.println("Loaded Customer: " + count));
  }

  private void loadBeerData() {
    beerRepository.count().subscribe(count -> {
      if (count == 0) {
        createBeer("Galaxy Cat", BeerStyle.LAGER, "12356", 12.99, 122);
        createBeer("Crank", BeerStyle.PALE_ALE, "12356222", 11.99, 392);
        createBeer("Sunshine City", BeerStyle.IPA, "12356", 13.99, 144);
      }
    });
  }

  private void loadCustomerData() {
    customerRepository.count().subscribe(count -> {
      if (count == 0) {
        createCustomer("Jason Bourne");
        createCustomer("Kenny Rogers");
        createCustomer("Isabella Roselin");
      }
    });
  }


  private void createBeer(String name, BeerStyle style, String upc, double price, int quantity) {
    createBeer(name, style, upc, BigDecimal.valueOf(price), quantity);
  }

  private void createBeer(String name, BeerStyle style, String upc, BigDecimal price,
      int quantity) {

    Beer beer = Beer.builder()
        .beerName(name)
        .beerStyle(style.name())
        .upc(upc)
        .price(price)
        .quantityOnHand(quantity)
        .build();

    beerRepository.save(beer).subscribe();
  }

  private void createCustomer(String name) {
    customerRepository.save(Customer.builder()
            .customerName(name)
            .build())
        .subscribe();
  }
}
