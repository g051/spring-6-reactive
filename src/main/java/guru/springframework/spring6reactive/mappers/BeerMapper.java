package guru.springframework.spring6reactive.mappers;

import guru.springframework.spring6reactive.domain.Beer;
import guru.springframework.spring6reactive.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

  BeerDTO beerToBeerDTO(Beer beer);

  Beer beerDTOToBeer(BeerDTO dto);
}
