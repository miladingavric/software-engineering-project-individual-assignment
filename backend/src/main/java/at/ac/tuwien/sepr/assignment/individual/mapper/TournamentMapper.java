package at.ac.tuwien.sepr.assignment.individual.mapper;

import at.ac.tuwien.sepr.assignment.individual.dto.HorseDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentListDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentSearchDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Horse;
import at.ac.tuwien.sepr.assignment.individual.entity.Tournament;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.Optional;
@Component
public class TournamentMapper {
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /**
   * Convert a horse entity object to a {@link TournamentListDto}.
   * The given map of owners needs to contain the owner of {@code horse}.
   *
   * @param tournament the horse to convert
   * @return the converted {@link TournamentListDto}
   */
  public TournamentListDto entityToListDto(Tournament tournament) {
    LOG.trace("entityToListDto({})", tournament);
    if (tournament == null) {
      return null;
    }
    HorseDetailDto[] dtoHorses = new  HorseDetailDto[tournament.getParticipants().length];
    int increment = 0;
    for (Horse participant : tournament.getParticipants()) {
      dtoHorses[increment] = new HorseDetailDto(participant.getId(),
          participant.getName(),
          participant.getSex(),
          participant.getDateOfBirth(),
          participant.getHeight(),
          participant.getWeight(),
          null);
      increment++;
    }
    return new TournamentListDto(
        tournament.getId(),
        tournament.getName(),
        tournament.getStartDate(),
        tournament.getEndDate(),
        dtoHorses
    );

  }

  public TournamentDetailDto entityToDetailDto(Tournament tournament) {
    LOG.trace("entityToListDto({})", tournament);
    if (tournament == null) {
      return null;
    }
    HorseDetailDto[] dtoHorses = new  HorseDetailDto[tournament.getParticipants().length];
    int increment = 0;
    for (Horse participant : tournament.getParticipants()) {
      dtoHorses[increment] = new HorseDetailDto(participant.getId(),
          participant.getName(),
          participant.getSex(),
          participant.getDateOfBirth(),
          participant.getHeight(),
          participant.getWeight(),
          null);
      increment++;
    }
    return new TournamentDetailDto(
        tournament.getId(),
        tournament.getName(),
        tournament.getStartDate(),
        tournament.getEndDate(),
        dtoHorses
    );
  }
}
