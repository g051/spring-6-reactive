package guru.springframework.spring6reactive.services;

import guru.springframework.spring6reactive.mappers.BeerMapper;
import guru.springframework.spring6reactive.model.BeerDTO;
import guru.springframework.spring6reactive.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

  private final BeerRepository beerRepository;
  private final BeerMapper beerMapper;

  @Override
  public Flux<BeerDTO> listBeers() {
    return beerRepository.findAll()
        .map(beerMapper::beerToBeerDTO);
  }

  @Override
  public Mono<BeerDTO> getBeerById(Integer id) {
    return beerRepository.findById(id)
        .map(beerMapper::beerToBeerDTO);
  }

  @Override
  public Mono<BeerDTO> createBeer(BeerDTO beerDTO) {
    return beerRepository.save(beerMapper.beerDTOToBeer(beerDTO))
        .map(beerMapper::beerToBeerDTO);
  }

  @Override
  public Mono<BeerDTO> updateBeer(Integer id, BeerDTO beerDTO) {
    return beerRepository.findById(id)
        .map(foundBeer -> {
          foundBeer.setBeerName(beerDTO.getBeerName());
          foundBeer.setBeerStyle(beerDTO.getBeerStyle());
          foundBeer.setPrice(beerDTO.getPrice());
          foundBeer.setUpc(beerDTO.getUpc());
          foundBeer.setQuantityOnHand(beerDTO.getQuantityOnHand());
          return foundBeer;
        }).flatMap(beerRepository::save)
        .map(beerMapper::beerToBeerDTO);
  }

  @Override
  public Mono<BeerDTO> patchBeer(Integer id, BeerDTO beerDTO) {
    return beerRepository.findById(id)
        .map(foundBeer -> {
          if (StringUtils.hasText(beerDTO.getBeerName())) {
            foundBeer.setBeerName(beerDTO.getBeerName());
          }
          if (StringUtils.hasText(beerDTO.getBeerStyle())) {
            foundBeer.setBeerStyle(beerDTO.getBeerStyle());
          }
          if (beerDTO.getPrice() != null) {
            foundBeer.setPrice(beerDTO.getPrice());
          }
          if (StringUtils.hasText(beerDTO.getUpc())) {
            foundBeer.setUpc(beerDTO.getUpc());
          }
          if (beerDTO.getQuantityOnHand() != null) {
            foundBeer.setQuantityOnHand(beerDTO.getQuantityOnHand());
          }
          return foundBeer;
        }).flatMap(beerRepository::save)
        .map(beerMapper::beerToBeerDTO);
  }

  @Override
  public Mono<Void> deleteBeerById(Integer id) {
    return beerRepository.deleteById(id);
  }
}
