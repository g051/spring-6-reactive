package guru.springframework.spring6reactive.controllers;

import guru.springframework.spring6reactive.model.BeerDTO;
import guru.springframework.spring6reactive.services.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class BeerController {

  public static final String BEER_PATH = "/api/v2/beer";
  public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

  private final BeerService beerService;

  @GetMapping(BEER_PATH)
  Flux<BeerDTO> listBeers() {
    return beerService.listBeers();
  }

  @GetMapping(BEER_PATH_ID)
  Mono<BeerDTO> getBeerById(@PathVariable("beerId") Integer id) {
    return beerService.getBeerById(id);
  }

  @PostMapping(BEER_PATH)
  Mono<ResponseEntity<Void>> createBeer(@Validated @RequestBody BeerDTO beerDTO, ServerHttpRequest request) {
    return beerService.createBeer(beerDTO)
        .map(savedDTO -> ResponseEntity.created(UriComponentsBuilder
                .fromUri(request.getURI())
                .path("/{id}")
                .buildAndExpand(savedDTO.getId())
                .toUri())
            .build());
  }

  @PutMapping(BEER_PATH_ID)
  Mono<ResponseEntity<Void>> UpdateBeer(
      @PathVariable("beerId") Integer id,
      @Validated @RequestBody BeerDTO beerDTO) {
    return beerService.updateBeer(id, beerDTO)
        .map(savedDTO -> ResponseEntity.ok().build());
  }

  @PatchMapping(BEER_PATH_ID)
  Mono<ResponseEntity<Void>> patchBeer(
      @PathVariable("beerId") Integer id,
      @Validated @RequestBody BeerDTO beerDTO) {
    return beerService.patchBeer(id, beerDTO)
        .map(savedDTO -> ResponseEntity.ok().build());
  }

  @DeleteMapping(BEER_PATH_ID)
  Mono<ResponseEntity<Void>> deleteBeerById(@PathVariable("beerId") Integer id) {
    return beerService.deleteBeerById(id)
        .map(response -> ResponseEntity.noContent().build());
  }
}
