package at.ac.tuwien.sepr.assignment.individual.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public record TournamentStandingsDto(
        long id,
        String name,
        TournamentDetailParticipantDto[] participants,
        TournamentStandingsTreeDto tree
) {
  public TournamentStandingsDto withId(long newId) {
    return new TournamentStandingsDto(
        newId,
        name,
        participants,
        tree);
  }
}
