package at.ac.tuwien.sepr.assignment.individual.mapper;

import at.ac.tuwien.sepr.assignment.individual.dto.BreedDto;
import at.ac.tuwien.sepr.assignment.individual.dto.HorseDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.HorseListDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Horse;
import at.ac.tuwien.sepr.assignment.individual.exception.FatalException;
import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HorseMapper {
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /**
   * Convert a horse entity object to a {@link HorseListDto}.
   * The given map of owners needs to contain the owner of {@code horse}.
   *
   * @param horse the horse to convert
   * @param breeds a map of breeds identified by their id, required for mapping horses
   * @return the converted {@link HorseListDto}
   */
  public HorseListDto entityToListDto(Horse horse, Map<Long, BreedDto> breeds) {
    LOG.trace("entityToListDto({})", horse);
    if (horse == null) {
      return null;
    }

    var breed = Optional.of(breeds.get(horse.getBreedId()))
        .orElseThrow(() -> new FatalException(
            "Saved horse with id " + horse.getId() + " refers to non-existing breed with id " + horse.getBreedId()));

    return new HorseListDto(
        horse.getId(),
        horse.getName(),
        horse.getSex(),
        horse.getDateOfBirth(),
        breed
    );
  }

  /**
   * Convert a horse entity object to a {@link HorseListDto}.
   * The given map of owners needs to contain the owner of {@code horse}.
   *
   * @param horse the horse to convert
   * @return the converted {@link HorseListDto}
   */
  public HorseDetailDto entityToDetailDto(Horse horse, Map<Long, BreedDto> breeds) {
    LOG.trace("entityToDto({})", horse);
    if (horse == null) {
      return null;
    }

    var breed = Optional.of(breeds.get(horse.getBreedId()))
        .orElseThrow(() -> new FatalException(
            "Saved horse with id " + horse.getId() + " refers to non-existing breed with id " + horse.getBreedId()));

    return new HorseDetailDto(
        horse.getId(),
        horse.getName(),
        horse.getSex(),
        horse.getDateOfBirth(),
        horse.getHeight(),
        horse.getWeight(),
        breed
    );
  }
}
