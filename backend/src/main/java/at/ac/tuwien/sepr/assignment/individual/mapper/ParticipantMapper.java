package at.ac.tuwien.sepr.assignment.individual.mapper;

import at.ac.tuwien.sepr.assignment.individual.dto.TournamentDetailParticipantDto;
import at.ac.tuwien.sepr.assignment.individual.entity.Participant;
import org.springframework.stereotype.Component;


@Component
public class ParticipantMapper {
  public TournamentDetailParticipantDto entityToTournamentDetailParticipantDto(Participant participant) {

    return new TournamentDetailParticipantDto(
        participant.getId(),
        participant.getName(),
        participant.getDateOfBirth(),
        participant.getEntryNumber(),
        participant.getRoundReached());
  }
  public TournamentDetailParticipantDto entityToTournamentDetailParticipantDtoPlusRoundReached(Participant participant) {

    return new TournamentDetailParticipantDto(
        participant.getId(),
        participant.getName(),
        participant.getDateOfBirth(),
        participant.getEntryNumber(),
        1);
  }
}
