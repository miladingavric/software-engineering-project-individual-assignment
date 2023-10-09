package at.ac.tuwien.sepr.assignment.individual.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public record TournamentStandingsDto(
        long id,
        String name,
        TournamentDetailParticipantDto[] participants,
        TournamentStandingsTreeDto tree
) {
}
