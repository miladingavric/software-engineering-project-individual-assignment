package at.ac.tuwien.sepr.assignment.individual.mapper;

import at.ac.tuwien.sepr.assignment.individual.dto.TournamentDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentDetailParticipantDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentListDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentStandingsDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentStandingsTreeDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Horse;
import at.ac.tuwien.sepr.assignment.individual.entity.Tournament;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

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
    return new TournamentListDto(
            tournament.getId(),
            tournament.getName(),
            tournament.getStartDate(),
            tournament.getEndDate()
    );
  }


  public TournamentDetailDto entityToDetailDto(Tournament tournament) {
    LOG.trace("entityToDetailDto({})", tournament);
    Horse[] horses = tournament.getParticipants();
    TournamentDetailParticipantDto[] horseDtos = new TournamentDetailParticipantDto[horses.length];
    int increment = 0;
    for (Horse horse : horses) {
      horseDtos[increment] = new TournamentDetailParticipantDto(
                    horse.getId(),
                    horse.getName(),
                    horse.getDateOfBirth(),
                   -1,
                    -1
      );
      increment++;
    }
    return new TournamentDetailDto(
            tournament.getId(),
            tournament.getName(),
            tournament.getStartDate(),
            tournament.getEndDate(),
            horseDtos
    );
  }

  public TournamentStandingsDto entityToStandingsDto(Tournament tournament, TournamentStandingsTreeDto tree, long entryNumber, long roundReached) {
    Horse[] horses = tournament.getParticipants();
    TournamentDetailParticipantDto[] participants = new TournamentDetailParticipantDto[horses.length];
    int increment = 0;
    for (Horse horse : horses) {
      participants[increment] = new TournamentDetailParticipantDto(
              horse.getId(),
              horse.getName(),
              horse.getDateOfBirth(),
              entryNumber,
              roundReached
      );
      increment++;
    }
    return new TournamentStandingsDto(
            tournament.getId(),
            tournament.getName(),
            participants,
            tree
    );
  }
}
