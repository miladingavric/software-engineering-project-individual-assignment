package at.ac.tuwien.sepr.assignment.individual.dto;

import java.time.LocalDate;
import java.util.Date;

public record TournamentCreateDto(
        String name,
        LocalDate startDate,
        LocalDate endDate,
        HorseSelectionDto[] participants
) {
}
