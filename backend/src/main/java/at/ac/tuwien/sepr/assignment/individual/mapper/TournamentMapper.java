package at.ac.tuwien.sepr.assignment.individual.mapper;

import at.ac.tuwien.sepr.assignment.individual.dto.TournamentDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentDetailParticipantDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentListDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentStandingsDto;
import at.ac.tuwien.sepr.assignment.individual.dto.TournamentStandingsTreeDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Horse;
import at.ac.tuwien.sepr.assignment.individual.entity.Match;
import at.ac.tuwien.sepr.assignment.individual.entity.Participant;
import at.ac.tuwien.sepr.assignment.individual.entity.Tournament;
import at.ac.tuwien.sepr.assignment.individual.exception.NotFoundException;
import at.ac.tuwien.sepr.assignment.individual.persistence.HorseDao;
import at.ac.tuwien.sepr.assignment.individual.persistence.ParticipantDao;
import at.ac.tuwien.sepr.assignment.individual.persistence.TournamentDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.Collection;
import java.util.Random;

@Component
public class TournamentMapper {
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final ParticipantDao participantDao;
  public TournamentMapper(ParticipantDao participantDao) {
    this.participantDao = participantDao;
  }

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


  public TournamentDetailDto entityToDetailDto(Tournament tournament) throws NotFoundException {
    LOG.trace("entityToDetailDto({})", tournament);
    Participant[] horses = tournament.getParticipants();
    Collection<Participant> participantsInTournament = participantDao.getParticipantsInTournament(tournament.getId());
    TournamentDetailParticipantDto[] horseDtos = new TournamentDetailParticipantDto[horses.length];
    int increment = 0;
    for (Participant participant : participantsInTournament) {
      horseDtos[increment] = new TournamentDetailParticipantDto(
                    participant.getId(),
                    participant.getName(),
                    participant.getDateOfBirth(),
                    participant.getEntryNumber(),
                    participant.getRoundReached()
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

  public TournamentDetailParticipantDto entityToTournamentDetailParticipantDto(Participant participant) {
    return new TournamentDetailParticipantDto(
        participant.getId(),
        participant.getName(),
        participant.getDateOfBirth(),
        participant.getEntryNumber(),
        participant.getRoundReached());
  }

  public TournamentStandingsDto entityToStandingsDto(Tournament tournament, TournamentStandingsTreeDto tree) {
    Participant[] horses = tournament.getParticipants();
    TournamentDetailParticipantDto[] participants = new TournamentDetailParticipantDto[horses.length];
    int increment = 0;
    for (Participant horse : horses) {
      participants[increment] = entityToTournamentDetailParticipantDto(horse);
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
