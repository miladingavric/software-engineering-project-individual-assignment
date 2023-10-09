package at.ac.tuwien.sepr.assignment.individual.dto;

import java.time.LocalDate;
import java.util.Date;

public record TournamentDetailParticipantDto(
        long horseId,
        String name,
        LocalDate dateOfBirth,
        long entryNumber,
        long roundReached
) {
}
